package orioni.jz.io.files;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * This class allows streaming output to {@link RandomAccessFile} objects.  It does this by accepting data in a standard
 * streaming fashion, buffering it to a specified degree, and then writing it in units to the {@link RandomAccessFile}
 * in question.  Instances of this class are not invalid if the {@link RandomAccessFile}'s index pointer is modified,
 * but they are unreliable unless the {@link RandomAccessFileOutputStream#flush()} method has been called immediately
 * beforehand.
 *
 * @author Zachary Palmer
 */
public class RandomAccessFileOutputStream extends OutputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

	/** The {@link RandomAccessFile} to which data will be written. */
	protected RandomAccessFile raf;
	/** The output buffer for this {@link RandomAccessFileOutputStream}. */
	protected byte[] buffer;
	/** The number of bytes currently stored within the input buffer. */
	protected int bufferUsed;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

	/**
	 * General constructor.
	 * @param raf The {@link RandomAccessFile} to which data will be written.
	 * @param buffer The size (in bytes) of the output buffer.
	 */
	public RandomAccessFileOutputStream(RandomAccessFile raf, int buffer)
	{
		super();
		this.raf = raf;
		this.buffer = new byte[buffer];
		bufferUsed = 0;
	}

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

	/**
	 * Writes the specified byte to this output stream. The general contract for <code>write</code> is that one byte is
	 * written to the output stream. The byte to be written is the eight low-order bits of the argument <code>b</code>.
	 * The 24 high-order bits of <code>b</code> are ignored.
	 *
	 * @param b The <code>byte</code> to write.
	 * @throws IOException  if an I/O error occurs.
	 */
	public void write(int b) throws IOException
	{
		if (raf == null) throw new IOException("Stream closed.");
		buffer[bufferUsed++] = (byte) b;
		if (bufferUsed == buffer.length)
		{
			flush();
		}
	}

	/**
	 * Closes this output stream.  This method implicitly flushes the stream.  All future writes to this stream will
	 * throw an {@link IOException}.
	 * @throws IOException If the implicit {@link RandomAccessFileOutputStream#flush()} throws an {@link IOException}.
	 *                     An {@link IOException} will <i>not</i> be thrown if the stream is already closed.
	 */
	public void close() throws IOException
	{
		if (raf != null)
		{
			this.flush();
			raf = null;
			buffer = null;
		}
	}

	/**
	 * Flushes this output stream and forces any buffered output bytes to be written out.
	 * @throws IOException If an I/O error occurs while writing buffered data.
	 */
	public void flush() throws IOException
	{
		raf.write(buffer, 0, bufferUsed);
		bufferUsed = 0;
	}

	/**
	 * Writes <code>b.length</code> bytes from the specified byte array to this output stream.
	 * @param data The data to be written.
	 * @throws IOException If an I/O error occurs while writing the data.
	 */
	public void write(byte[] data) throws IOException
	{
		if (raf == null) throw new IOException("Stream closed.");
		this.write(data, 0, data.length);
	}

	/**
	 * Writes <code>len</code> bytes from the specified byte array starting at offset <code>off</code> to this output
	 * stream.
	 * @param data The data to write.
	 * @param off The start offset in the data.
	 * @param len The number of bytes to write.
	 * @throws IOException If an I/O error occurs while writing the data.
	 * @throws IndexOutOfBoundsException If either <code>off</code> or <code>len</code> is less than <code>0</code> or
	 *                                   if <code>off+len&gt;=data.length</code>.
	 */
	public void write(byte[] data, int off, int len) throws IOException
	{
		if (raf == null) throw new IOException("Stream closed.");
		while (len + bufferUsed >= buffer.length)
		{
			int toWrite = buffer.length - bufferUsed;
			System.arraycopy(data, off, buffer, bufferUsed, toWrite);
			len -= toWrite;
			off += toWrite;
			bufferUsed += toWrite;
			flush();
		}
		System.arraycopy(data, off, buffer, bufferUsed, len);
		bufferUsed += len;
	}

	/**
	 * Flushes and closes this stream.
	 */
	protected void finalize() throws Throwable
	{
		super.finalize();
		this.close();
	}

// STATIC METHODS ////////////////////////////////////////////////////////////////

}