package orioni.jz.util;

import orioni.jz.math.MutableInteger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * This class is designed to represent a data store in which multiple units of data are located.  Each data block is
 * indexed by a table of contents, which is located at the beginning of the store.  When writing a data block to the
 * store, a byte array is submitted along with an index number (which must be zero or above).  This is effectively a
 * mapping between the index number and the byte array.  To properly store this abstract data, a {@link BlockDataStore}
 * is provided to the <code>BlockDataMap</code> on construction. <P> If the index number is not currently mapped, the
 * indexing table grows to accomodate it; otherwise, the data already mapped to that index is destroyed.  The data block
 * is written to an arbitrary position in the store, depending on the size of free spaces in the store.  When data
 * blocks are retrieved, they are simply read from the position at which the table of contents shows them. <P> Data
 * blocks can also be deleted, causing them to be removed entirely from the store.  The rewriting or deleting of data
 * blocks in the data store will frequently cause the data store to contain wasted space (since it would be inefficient
 * to reclaim wasted space after every write operation).  The <code>repack()</code> method of this class allows the user
 * to reclaim the wasted space in the store.  Once called, an instance of this class will perform all work necessary to
 * ensure that no space in the data store is wasted.  Optionally, users can call <code>repack(int)</code> to allow some
 * space for the table of contents to grow. <P> The table of contents is formatted as follows: <UL>
 * <LI><code>data_offset</code>: an 8-byte value that represents the offset at which data begins in the store.  Since
 * the table of contents may not occupy all of its allocated space, this value is used to prevent data blocks from
 * consuming space set aside for the TOC. <LI><code>toc_entries_used</code>: a 4-byte value indicating the number of TOC
 * entry spaces that are actually in use. All of the remaining TOC entries are unusued and can be occupied by later
 * writes. <LI><code>TOC_entry</code> (repeated until the offset <code>data_offset</code> is reached): <UL>
 * <LI><code>map_number</code>: a 4-byte value representing the key of this <code>TOC_entry</code>.  The key is the
 * value passed to <code>read(int)</code> to retrieve the data block.  If this value is less than <code>0</code>, it
 * represents an entry in the TOC which is not currently being used. <LI><code>offset</code>: an 8-byte value
 * representing the starting offset of the data represented by this <code>TOC_entry</code> <i>from the beginning of the
 * store</i>.  This value will never be less than <code>data_offset</code>. <LI><code>size</code>: a 4-byte value
 * representing the size of the data represented by this <code>TOC_entry</code>. </UL> </UL> Following the table of
 * contents is a data block of any size up to the limit of the data store or to a total store size of 8,388,608
 * terabytes.  The specific format of indexing storage and other such information is dependent upon the storage medium.
 * <P> Because the storage and retrieval of data for an instance of this class relies on a single {@link BlockDataStore}
 * instance, any methods which adjust its store pointer are synchronized.  As a result, use of a
 * <code>BlockDataMap</code> with multiple threads is safe.
 *
 * @author Zachary Palmer
 */
public class BlockDataMap
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * The size of the TOC header, in bytes.
     */
    public static final int TOC_HEADER_SIZE = 12;
    /**
     * The size of a TOC entry, in bytes.
     */
    public static final int TOC_ENTRY_SIZE = 16;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link BlockDataStore} that will be used for data storage and retrieval.
     */
    protected BlockDataStore dataStore;

    /**
     * The offset in the store at which data begins.
     */
    protected long dataOffset;
    /**
     * An array containing all of the <code>TocEntry</code> objects for the table of contents.  This data structure
     * contains an object for every TOC entry in the store, regardless of whether or not it is occupied.
     */
    protected TocEntry[] toc;
    /**
     * A mapping for the TOC, indexed by <code>TocEntry</code> mapping number.  This data structure only contains
     * references to occupied TOC entries.
     */
    protected HashMap<MutableInteger, TocEntry> tocMap;
    /**
     * A tree set for the TOC, allowing space between data blocks to be found.  This data structure only contains
     * references to occupied TOC entries.
     */
    protected TreeSet<TocEntry> tocSet;
    /**
     * The number of TOC entries that exist.
     */
    protected int tocSize;
    /**
     * The number of TOC entries in use.
     */
    protected int tocEntriesUsed;

    /**
     * A <code>MutableInteger</code> used to prevent frequent object creation when accessing the TOC map.
     */
    protected MutableInteger tocMapKey;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes that the data in the {@link BlockDataStore} provided will be used to initialize
     * this map.
     *
     * @param store The {@link BlockDataStore} that will be used to contain the data for this map.
     * @throws java.io.IOException If an I/O error occurs in the provided {@link BlockDataStore} while reading the table
     *                             of contents, or if the data in the store is incorrectly formatted.
     */
    public BlockDataMap(BlockDataStore store)
            throws IOException
    {
        this(store, false);
    }

    /**
     * Full constructor.
     *
     * @param store The {@link BlockDataStore} that will be used to contain the data for this map.
     * @param clear <code>true</code> if this data map should clear the store before completion construction;
     *              <code>false</code> if this data map should use the data in the data store for its mappings.
     * @throws java.io.IOException If an I/O error occurs in the provided {@link BlockDataStore} while reading the table
     *                             of contents, or if the data in the store is incorrectly formatted.
     */
    public BlockDataMap(BlockDataStore store, boolean clear)
            throws IOException
    {
        synchronized (this)
        {
            // Initialize instance fields
            dataStore = store;
            tocMap = new HashMap<MutableInteger, TocEntry>();
            tocSet = new TreeSet<TocEntry>();
            tocMapKey = new MutableInteger();

            // If the store was just created, initialize it.
            if ((dataStore.getSize() == 0) || clear)
            {
                initialize();
            }

            // Load all necessary information from the store and expand it into the necessary data structures.
            dataStore.seek(0);
            dataOffset = dataStore.readLong();
            tocEntriesUsed = dataStore.readInt();
            if (dataOffset > dataStore.getSize())
            {
                throw new IOException("Storage format error: bad starting data offset.");
            }
            tocSize = (int) (dataOffset - TOC_HEADER_SIZE) / TOC_ENTRY_SIZE;   // 16 byte lead-in, 20 bytes per entry
            toc = new TocEntry[tocSize];
            for (int i = 0; i < tocEntriesUsed; i++)
            {
                int mapping = dataStore.readInt();
                long offset = dataStore.readLong();
                int size = dataStore.readInt();
                TocEntry entry = new TocEntry(i, new MutableInteger(mapping), offset, size);
                toc[i] = entry;
                if (mapping >= 0)
                {
                    tocSet.add(entry);
                    tocMap.put(entry.getMapping(), entry);
                }
            }
            for (int j = tocEntriesUsed; j < tocSize; j++)
            {
                toc[j] = new TocEntry(j, new MutableInteger(-1), 0, 0);
            }
        }
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Initializes the data map.  This method destroys all existing entries within the data store and packs it to empty
     * the file.
     *
     * @throws IOException If an error occurs during initialization.
     */
    public synchronized void initialize()
            throws IOException
    {
        dataStore.seek(0);
        dataStore.writeLong(TOC_HEADER_SIZE + TOC_ENTRY_SIZE);
        dataStore.writeInt(0);    // number of TOC entries in use
        dataStore.writeInt(-1);   // TOC entry 0: empty key
        dataStore.writeLong(0);   // TOC entry 0: starting offset
        dataStore.writeInt(0);    // TOC entry 0: size of data
        dataStore.setLength(28);  // The size of the data above
    }

    /**
     * Retrieves the data block stored at the given mapping. <P> This method is synchronized to ensure safe
     * multi-threaded access to the underlying {@link BlockDataStore}.
     *
     * @param mapping A value between <code>0</code> and <code>Integer.MAX_VALUE</code> indicating the data block to
     *                retrieve.
     * @return A byte array containing the data from the specified block, or <code>null</code> if the mapping does not
     *         indicate a valid data block.
     * @throws java.lang.IllegalArgumentException
     *                             If the value of <code>mapping</code> is less than zero.
     * @throws java.io.IOException If an I/O error occurs in the underlying {@link BlockDataStore}.
     */
    public synchronized byte[] read(int mapping)
            throws IOException
    {
        if (dataStore == null) throw new IOException("BlockDataMap has been closed.");
        if (mapping < 0) throw new IllegalArgumentException("Invalid mapping on call to read(int): " + mapping);
        tocMapKey.setValue(mapping);
        TocEntry entry = tocMap.get(tocMapKey);
        if (entry == null)
        {
            // The mapping is empty.
            return null;
        }
        dataStore.seek(entry.getOffset());
        byte[] data = new byte[entry.getSize()];
        dataStore.readByteArray(data);
        return data;
    }

    /**
     * Deletes the data block stored at the given mapping. <P> This method is synchronized to ensure safe multi-threaded
     * access to the underlying {@link BlockDataStore}.
     *
     * @param mapping A value between <code>0</code> and <code>Integer.MAX_VALUE</code> indicating the data block to
     *                delete.
     * @throws java.lang.IllegalArgumentException
     *                             If the value of <code>mapping</code> is less than zero.
     * @throws java.io.IOException If an I/O error occurs in the underlying {@link BlockDataStore}.
     */
    public synchronized void delete(int mapping)
            throws IOException
    {
        if (dataStore == null) throw new IOException("BlockDataMap has been closed.");
        if (mapping < 0) throw new IllegalArgumentException("Invalid mapping on call to delete(int): " + mapping);
        tocMapKey.setValue(mapping);
        TocEntry entry = tocMap.get(tocMapKey);
        if (entry == null)
        {
            // The mapping is empty.
            throw new IOException("Unmapped mapping on call to delete(int): " + mapping);
        }
        TocEntry lastEntry = toc[tocEntriesUsed - 1];
        // Remove the entry from the data structures
        swapTocEntries(entry, lastEntry);
        tocMap.remove(entry.getMapping());
        tocSet.remove(entry);
        // Void the entry to be deleted.
        entry.getMapping().setValue(-1);
        entry.setOffset(0);
        entry.setSize(0);
        // Decrement the number of entries used
        tocEntriesUsed--;
        // Perform the necessary disk updates
        diskUpdateTocInUseValue();
        diskUpdateTocEntry(lastEntry); // (no longer actually the last entry)
        diskUpdateTocEntry(entry);
    }

    /**
     * Writes a data block to the given mapping.  This may cause an increase in the size of the store.  If a data block
     * already exists at the given mapping, it will be deleted first.  No guarantee is made that the new data block will
     * occupy all or any of the space originally occupied by the old data block. <P> If the data block replaces an older
     * one and is not identical in size to the older data block, the store will accumulate some wasted space.  This can
     * be cleaned up by using the <code>repack()</code> and <code>repack(int)</code> methods.  This is not done
     * automatically because repacking the store is expensive and time consuming. <P> In the event that the TOC runs out
     * of space for entries, this method may reorganize the contents of the store. This may require reading in any one
     * data block in the store (including the largest); it is thus important to ensure that such memory is free before
     * calling this method.  For example, if the store contains a 512K block, a 2M block, and a 1M block, at least 2M of
     * memory should be free before calling <code>write(int,byte[])</code>.
     *
     * @param mapping A value between <code>0</code> and <code>Integer.MAX_VALUE</code> indicating the data block to
     *                which data will be written.
     * @param data    The <code>byte[]</code> containing the data to write to this mapping.
     * @throws java.lang.IllegalArgumentException
     *                             If the value of <code>mapping</code> is less than zero.
     * @throws java.io.IOException If an I/O error occurs in the underlying {@link BlockDataStore}.
     */
    public synchronized void write(int mapping, byte[] data)
            throws IOException
    {
        if (dataStore == null) throw new IOException("BlockDataMap has been closed.");
        if (mapping < 0) throw new IllegalArgumentException("Invalid mapping on call to read(int): " + mapping);

        // If an entry for this mapping exists, delete it first
        tocMapKey.setValue(mapping);
        TocEntry entry = tocMap.get(tocMapKey);
        if (entry != null)
        {
            delete(mapping);
        }

        // Determine which TOC entry will be used
        if (tocEntriesUsed >= tocSize)
        {
            // There are no spaces left in the TOC; it must be expanded!
            if (tocSet.isEmpty())
            {
                // Well, -that- certainly makes things easier.  There's no data in the store.  Let's make room for some
                // more entries.
                dataOffset += TOC_ENTRY_SIZE * 5;
            } else
            {
                // Ensure that there is room for additional TOC entries.
                ensureTocEntries(1);
            }
            diskUpdateStartingDataOffset();
        }

        // Claim a TOC entry and write the data to the store.
        entry = toc[tocEntriesUsed++];
        if (tocSet.isEmpty())
        {
            entry.setOffset(dataOffset);
        } else
        {
            TocEntry lastEntry = tocSet.last();
            entry.setOffset(lastEntry.getOffset() + lastEntry.getSize());
        }
        entry.setSize(data.length);
        entry.getMapping().setValue(mapping);
        dataStore.seek(entry.getOffset());
        dataStore.writeByteArray(data);
        diskUpdateTocEntry(entry);
        diskUpdateTocInUseValue();
        // Update memory structures to reflect the change.
        tocMap.put(entry.getMapping(), entry);
        tocSet.add(entry);
    }

    /**
     * This method closes the underlying {@link BlockDataStore}.  Calls to the store manipulation methods will now cause
     * <code>IOException</code>s to be thrown.
     *
     * @throws java.io.IOException If an I/O error occurs in the underlying {@link BlockDataStore}.
     */
    public synchronized void close()
            throws IOException
    {
        dataStore.close();
        dataStore = null;
    }

    /**
     * Repacks the data store.  This recovers wasted space between data blocks and at the portion of the data store
     * between the TOC and the first block of data.
     *
     * @param indexSpace The amount of space, in index records, to leave between the TOC and the data blocks to allow
     *                   for store expansion.
     * @throws IOException              If an I/O error occurs in the underlying {@link BlockDataStore}.
     * @throws IllegalArgumentException If the <code>index_space</code> parameter is less than zero.
     */
    public synchronized void repack(int indexSpace)
            throws IOException, IllegalArgumentException
    {
        if (indexSpace < 0) throw new IllegalArgumentException("Parameter cannot be less than zero.");
        ensureTocEntries(indexSpace);
        if (indexSpace + tocEntriesUsed < tocSize)
        {
            // The current TocEntry array is too big.  Shorten it.
            TocEntry[] newToc = new TocEntry[indexSpace + tocEntriesUsed];
            System.arraycopy(toc, 0, newToc, 0, newToc.length);
            toc = newToc;
            tocSize = toc.length;
        }
        dataOffset = TOC_HEADER_SIZE + (tocEntriesUsed + indexSpace) * TOC_ENTRY_SIZE;
        diskUpdateStartingDataOffset();

        // Now pack the data nice and tight.
        long currentOffset = dataOffset;
        for (int i = 0; i < tocEntriesUsed; i++)
        {
            forceFreeSpace(currentOffset, toc[i].getSize());
            moveDataBlock(i, currentOffset);
            currentOffset += toc[i].getSize();
        }

        // Packing complete.  Truncate the block data store.
        dataStore.setLength(currentOffset);
    }

// INTERNAL NON-STATIC METHODS ///////////////////////////////////////////////////

    /**
     * Ensures that there are at least a given number of unused TOC entries available at the beginning of the store.
     * This is done by moving data blocks to the end of the store until the appropriate number are available.
     *
     * @param count The number of TOC entries that should be available.
     * @throws java.io.IOException If an I/O error occurs in the underlying {@link BlockDataStore}.
     * @throws java.lang.IndexOutOfBoundsException
     *                             If there are no entries in this store.
     */
    protected synchronized void ensureTocEntries(int count)
            throws IOException, IndexOutOfBoundsException
    {
        TocEntry firstEntry = tocSet.first();
        int newEntries = count - (tocSize - tocEntriesUsed);
        if (newEntries <= 0) return;

        // Add new entries
        while (firstEntry.getOffset() < dataOffset + TOC_ENTRY_SIZE * newEntries)
        {
            moveFirstDataBlockToEndOfDataStore();
            firstEntry = tocSet.first();
        }

        // Now, consume as much space between the end of the TOC and the beginning of the data block as possible.
        dataOffset = firstEntry.getOffset();
        dataOffset -= TOC_HEADER_SIZE;
        dataOffset /= TOC_ENTRY_SIZE;
        dataOffset *= TOC_ENTRY_SIZE;
        dataOffset += TOC_HEADER_SIZE;
        diskUpdateStartingDataOffset();

        // Adjust TOC data structures appropriately
        tocSize += newEntries;
        TocEntry[] newToc = new TocEntry[tocSize];
        TocEntry[] oldToc = toc;
        toc = newToc;
        System.arraycopy(oldToc, 0, toc, 0, oldToc.length);
        for (int i = oldToc.length; i < toc.length; i++)
        {
            toc[i] = new TocEntry(i, new MutableInteger(-1), 0, 0);
            diskUpdateTocEntry(toc[i]);
        }
    }

    /**
     * Moves the first data block in the store to the end of the store.  This frees up space at the beginning of the
     * data segment. <P> <B>Access to this method is not synchronized!</B>
     *
     * @throws java.io.IOException If an I/O error occurs in the underlying {@link BlockDataStore}.
     * @throws java.lang.IndexOutOfBoundsException
     *                             If there are no entries in this store.
     */
    protected synchronized void moveFirstDataBlockToEndOfDataStore()
            throws IOException, IndexOutOfBoundsException
    {
        if (toc.length == 0) throw new IndexOutOfBoundsException("No entries in BlockDataMap");
        TocEntry firstEntry = tocSet.first();
        TocEntry lastEntry = tocSet.last();
        byte[] data = new byte[firstEntry.getSize()];
        dataStore.seek(firstEntry.getOffset());
        dataStore.readByteArray(data);
        dataStore.seek(lastEntry.getOffset() + lastEntry.getSize());
        dataStore.writeByteArray(data);
        tocSet.remove(firstEntry);
        firstEntry.setOffset(lastEntry.getOffset() + lastEntry.getSize());
        tocSet.add(firstEntry);
        diskUpdateTocEntry(firstEntry);
    }

    /**
     * Moves a data block in the store to a specific offset.  Its TOC entry is updated appropriately.  This method does
     * <b>not</b> check for collision between data blocks. <P> <B>Access to this method is not synchronized!</B>
     *
     * @param index  The index of the data block to move.
     * @param offset The offset to which to move the data block.
     * @throws java.io.IOException If an I/O error occurs in the underlying {@link BlockDataStore}.
     * @throws java.lang.IndexOutOfBoundsException
     *                             If the provided index is not a valid index in the store.
     */
    protected synchronized void moveDataBlock(int index, long offset)
            throws IOException, IndexOutOfBoundsException
    {
        // Fetch entry
        TocEntry entry = toc[index];
        byte[] data = new byte[entry.getSize()];
        dataStore.seek(entry.getOffset());
        dataStore.readByteArray(data);

        // Determine if moving the data block directly will be sufficient.
        if ((entry.getOffset() + entry.getSize() > offset) && (entry.getOffset() - entry.getSize() < offset))
        {
            // Write entry to temp space at the end of the store for safety's sake
            TocEntry lastEntry = tocSet.last();
            long eofOffset = lastEntry.getOffset() + lastEntry.getSize();
            dataStore.seek(eofOffset);
            dataStore.writeByteArray(data);
            entry.setOffset(eofOffset);
            diskUpdateTocEntry(entry);
        }

        // Now write the entry to its intended location
        dataStore.seek(offset);
        dataStore.writeByteArray(data);
        tocSet.remove(entry);
        entry.setOffset(offset);
        tocSet.add(entry);
        diskUpdateTocEntry(entry);
    }

    /**
     * Forces free space at the specified offset for the given number of bytes.  This allows a data block to be
     * positioned at that space without concern for collision.
     *
     * @param offset The offset at which free space will be placed.
     * @param amount The amount of free space to force at the given offset.
     * @throws java.io.IOException If an I/O error occurs in the underlying {@link BlockDataStore}.
     */
    protected synchronized void forceFreeSpace(long offset, long amount)
            throws IOException
    {
        TocEntry entry = getFirstEntryAt(offset);
        while ((entry != null) && (entry.getOffset() < offset + amount))
        {
            TocEntry lastEntry = tocSet.last();
            moveDataBlock(entry.getIndex(), lastEntry.getOffset() + lastEntry.getSize());
            entry = getFirstEntryAt(offset);
        }
    }

    /**
     * Retrieves the first TocEntry whose data block uses space at or after the specified offset.  If there is no such
     * TocEntry, <code>null</code> is returned.
     *
     * @param offset The start of the range in which to find part of a data block for any TocEntry.
     */
    public synchronized TocEntry getFirstEntryAt(long offset)
    {
        Iterator it = tocSet.iterator();
        TocEntry entry;
        while (it.hasNext())
        {
            entry = (TocEntry) (it.next());
            if (entry.getOffset() + entry.getSize() > offset) return entry;
        }
        return null;
    }

    /**
     * Determines the first offset at which the specified number of bytes of space is available.  It is possible that
     * this method will return an offset equal to the end of the store.
     *
     * @param size The number of bytes of space necessary.
     * @return The offset at which the specified number of bytes can be found.
     */
    protected synchronized long getFreeSpaceOffset(long size)
    {
        long currentOffset = dataOffset;
        for (TocEntry entry : tocSet)
        {
            if (entry.getOffset() - currentOffset >= size) return currentOffset;
            currentOffset = entry.getOffset() + entry.getSize();
        }
        return currentOffset;
    }

    /**
     * Swaps two entries in the entry table, performing the appropriate adjustments to the entries' indices.
     *
     * @param entryA The first entry to swap.
     * @param entryB The second entry to swap with the first.
     */
    protected synchronized void swapTocEntries(TocEntry entryA, TocEntry entryB)
    {
        int indexA = entryA.getIndex();
        int indexB = entryB.getIndex();
        entryB.setIndex(indexA);
        entryA.setIndex(indexB);
        toc[indexA] = entryB;
        toc[indexB] = entryA;
    }

    /**
     * Updates a given TOC entry on the disk.
     *
     * @param entry The TOC entry in the table that needs updated.
     * @throws java.io.IOException If an I/O error occurs in the underlying {@link BlockDataStore}.
     */
    protected synchronized void diskUpdateTocEntry(TocEntry entry)
            throws IOException
    {
        dataStore.seek(TOC_HEADER_SIZE + TOC_ENTRY_SIZE * entry.getIndex());
        dataStore.writeInt(entry.getMapping().intValue());
        dataStore.writeLong(entry.getOffset());
        dataStore.writeInt(entry.getSize());
    }

    /**
     * Updates the value in the store which represents the starting offset of the data block.
     *
     * @throws java.io.IOException If an I/O error occurs in the underlying {@link BlockDataStore}.
     */
    protected synchronized void diskUpdateStartingDataOffset()
            throws IOException
    {
        dataStore.seek(0);
        dataStore.writeLong(dataOffset);
    }

    /**
     * Updates the value in the store which represents the number of TOC entries in use.
     *
     * @throws java.io.IOException If an I/O error occurs in the underlying {@link BlockDataStore}.
     */
    protected synchronized void diskUpdateTocInUseValue()
            throws IOException
    {
        dataStore.seek(8);
        dataStore.writeInt(tocEntriesUsed);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

// CONTAINED CLASSES : TOCENTRY //////////////////////////////////////////////////

    /**
     * This class is designed for internal use in the <code>BlockDataMap</code> class and represents one
     * table-of-contents entry. <P> <code>TocEntry</code> implements <code>Comparable</code> to sort entries in order of
     * increasing data offset (value of <code>m_offset</code>).
     */
    public static class TocEntry implements Comparable
    {

        /**
         * The index of this <code>TocEntry</code> in the table of contents.
         */
        protected int index;
        /**
         * The mapping ID for this TOC entry.
         */
        protected MutableInteger mapping;
        /**
         * The offset in the store at which the data block represented by this TOC entry can be found.
         */
        protected long offset;
        /**
         * The size of the data block represented by this TOC entry.
         */
        protected int size;

        /**
         * General constructor.
         */
        TocEntry(int index, MutableInteger mapping, long dataOffset, int dataSize)
        {
            this.index = index;
            this.mapping = mapping;
            offset = dataOffset;
            size = dataSize;
        }

        public int getIndex()
        {
            return index;
        }

        public void setIndex(int index)
        {
            this.index = index;
        }

        public MutableInteger getMapping()
        {
            return mapping;
        }

        public void setMapping(MutableInteger mapping)
        {
            this.mapping = mapping;
        }

        public long getOffset()
        {
            return offset;
        }

        public void setOffset(long offset)
        {
            this.offset = offset;
        }

        public int getSize()
        {
            return size;
        }

        public void setSize(int size)
        {
            this.size = size;
        }

        /**
         * Compares this <code>TocEntry</code>'s data offset to another <code>TocEntry</code>'s data offset.  If the
         * other object is not a <code>TocEntry</code>, a <code>ClassCastException</code> is thrown.
         *
         * @param o The <code>TocEntry</code> entry to compare with this one.
         * @return <code>-1</code>, <code>0<code>, or <code>1</code>, if the data offset of this <code>TocEntry</code>
         *         is less than, equal to, or greater than the data offset of the <code>TocEntry</code> to which this
         *         <code>TocEntry</code> is being compared, repsectively.
         * @throws java.lang.ClassCastException If the specified object is not a <code>TocEntry</code>.
         */
        public int compareTo(Object o)
        {
            TocEntry other = (TocEntry) o;
            if (offset < other.getOffset()) return -1;
            if (offset > other.getOffset()) return 1;
            // If the store is in good format, this should never happen.
            return 0;
        }
    }

}

// END OF FILE //