package orioni.jz.io;

import orioni.jz.util.Utilities;

import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * This utilities class is designed to contain stream- and I/O-related operations.
 *
 * @author Zachary Palmer
 */
public class IOUtilities
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Private constructor.  Utilities classes should not be instantiated.
     */
    private IOUtilities()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Compares the contents of two streams to determine if they are equal.  This is the case if every byte in both
     * streams is identical and the streams are of the same length.
     *
     * @param a The first stream to compare.
     * @param b The second stream to compare.
     * @return <code>true</code> if the streams' contents were equal; <code>false</code> if not.
     * @throws IOException If an I/O error occurs while reading from the streams.
     */
    public static boolean compareStreams(InputStream a, InputStream b)
            throws IOException
    {
        byte[] bufferA = new byte[16384];
        byte[] bufferB = new byte[16384];
        int readA = a.read(bufferA);
        int readB = b.read(bufferB);
        while ((readA == readB) && (readA != -1))
        {
            if (!Utilities.deepEquals(bufferA, bufferB)) return false;
            readA = a.read(bufferA);
            readB = b.read(bufferB);
        }
        return (readA == -1) && (readB == -1);
    }

    /**
     * This method is used to perform a Diffie-Hellman key exchange.  It allows two parties communicating over a pair of
     * piped input and output streams to generate and agree upon an encryption key.  Third parties spying on the key
     * exchange will not be capable of discerning the key as a result of the difficulty of solving the discrete
     * logarithm problem.
     * <p/>
     * For this operation to succeed, both sides must be calling this method with the same key length parameter but
     * different <code>initiator</code> parameters.  Also, the writes of one party to the output stream it is provided
     * must be readable through the other party's input stream and vice versa.
     *
     * @param is        The {@link InputStream} through which input from the other side is received.
     * @param os        The {@link OutputStream} through which output to the other side is sent.
     * @param length    The length (in bytes) of the key to generate.
     * @param initiator <code>true</code> if the caller is initiating the key exchange; <code>false</code> if the remote
     *                  side is initiating the key exchange.
     * @throws IOException              If an I/O error occurred while performing the exchange.
     * @throws IllegalArgumentException If the provided length is less than one.
     */
    public static byte[] performDiffieHellmanExchange(InputStream is, OutputStream os, int length, boolean initiator)
            throws IOException
    {
        if (length < 1) throw new IllegalArgumentException("Provided length was less than one (" + length + ").");
        PrimitiveInputStream pis = new PrimitiveInputStream(is, PrimitiveInputStream.BIG_ENDIAN);
        PrimitiveOutputStream pos = new PrimitiveOutputStream(os, PrimitiveOutputStream.BIG_ENDIAN);

        BigInteger g = BigInteger.valueOf(5);
        Random random = new SecureRandom();

        BigInteger exchangedValue;

        if (initiator)
        {
            // Step 1: agree on a prime number
            BigInteger prime = null;
            while (prime == null)
            {
                prime = new BigInteger(length * 8 + 1, 100, random);
                // only accept the prime number if the uppermost bit is on
                if (!prime.testBit(length * 8)) prime = null;
            }
            pos.writeBigInteger(prime);

            // Step 2: generate local modulus-exponentiated prime
            BigInteger a = new BigInteger(length * 8, random);
            BigInteger powAModP = g.modPow(a, prime);

            // Step 3: send g^a mod p and receive g^b mod p
            pos.writeBigInteger(powAModP);
            BigInteger powBModP = pis.readBigInteger();

            // Step 4: ascertain the value of (g^a mod p)^b mod p = (g^b mod p)^a mod p
            exchangedValue = powBModP.modPow(a, prime);
        } else
        {
            // Step 1: agree on a prime number
            BigInteger prime = pis.readBigInteger();

            // Step 2: generate local modulus-exponentiated prime
            BigInteger b = new BigInteger(length * 8, random);
            BigInteger powBModP = g.modPow(b, prime);

            // Step 3: receive g^a mod p and send g^b mod p
            BigInteger powAModP = pis.readBigInteger();
            pos.writeBigInteger(powBModP);

            // Step 4: ascertain the value of (g^a mod p)^b mod p = (g^b mod p)^a mod p
            exchangedValue = powAModP.modPow(b, prime);
        }

        byte[] exchangedBytes = exchangedValue.toByteArray();
        byte[] ret = new byte[length];
        if (ret.length > exchangedBytes.length)
        {
            System.arraycopy(exchangedBytes, 0, ret, ret.length - exchangedBytes.length, exchangedBytes.length);
        } else
        {
            System.arraycopy(exchangedBytes, exchangedBytes.length - ret.length, ret, 0, ret.length);
        }
        return ret;
    }

    /**
     * Exhausts the provided stream, returning all of the data read from it.
     *
     * @param stream The {@link InputStream} from which to read the data.
     * @return The <code>byte[]</code> containing the data which was read.
     * @throws IOException If an I/O error occurs while reading from the stream.
     */
    public static byte[] readStreamContents(InputStream stream)
            throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        pumpStream(stream, baos);
        return baos.toByteArray();
    }

    /**
     * Reads all of the data from the provided {@link InputStream}, writing it to the provided {@link OutputStream}.
     * This operation will continue until the {@link InputStream} is exhausted or an {@link IOException} is thrown.
     *
     * @param is The {@link InputStream} containing data.
     * @param os The {@link OutputStream} to receive the data.
     * @return The number of bytes pumped.  Note that if the actual number of bytes is larger than 2^63-1, this return
     *         value will be inaccurate.
     * @throws IOException If an I/O error occurs during the operation.
     */
    public static long pumpStream(InputStream is, OutputStream os)
            throws IOException
    {
        return pumpStream(is, os, 16384);
    }

    /**
     * Reads all of the data from the provided {@link InputStream}, writing it to the provided {@link OutputStream}.
     * This operation will continue until the {@link InputStream} is exhausted or an {@link IOException} is thrown.
     *
     * @param is         The {@link InputStream} containing data.
     * @param os         The {@link OutputStream} to receive the data.
     * @param bufferSize The size of the buffer to use when pumping the stream.
     * @return The number of bytes pumped.  Note that if the actual number of bytes is larger than 2^63-1, this return
     *         value will be inaccurate.
     * @throws IOException If an I/O error occurs during the operation.
     */
    public static long pumpStream(InputStream is, OutputStream os, int bufferSize)
            throws IOException
    {
        return pumpStream(is, os, bufferSize, null, false);
    }

    /**
     * Reads all of the data from the provided {@link InputStream}, writing it to the provided {@link OutputStream}.
     * This operation will continue until the {@link InputStream} is exhausted or an {@link IOException} is thrown.
     *
     * @param is            The {@link InputStream} containing data.
     * @param os            The {@link OutputStream} to receive the data.
     * @param bufferSize    The size of the buffer to use when pumping the stream.
     * @param maxBytes      The maximum number of bytes to pump, or <code>null</code> if no maximum should be applied.
     * @param closeOnFinish <code>true</code> if the streams should be closed when pumping is complete;
     *                      <code>false</code> if they should not.  Note that the streams are closed upon completion
     *                      even if the {@link InputStream} is not exhausted (as a result of not reading
     *                      <code>maxBytes</code> bytes).
     * @return The number of bytes pumped.  Note that if the actual number of bytes is larger than 2^63-1, this return
     *         value will be inaccurate.
     * @throws IOException If an I/O error occurs during the operation.
     */
    public static long pumpStream(InputStream is, OutputStream os, int bufferSize, Long maxBytes, boolean closeOnFinish)
            throws IOException
    {
        byte[] buffer = new byte[bufferSize];
        long totalPumped = 0;
        int read;
        // Branch here instead of inside of the loop for sake of efficiency
        if (maxBytes == null)
        {
            while ((read = is.read(buffer)) != -1)
            {
                os.write(buffer, 0, read);
                totalPumped += read;
            }
        } else
        {
            while ((read = is.read(buffer, 0, (int) (Math.min(buffer.length, maxBytes - totalPumped)))) != -1)
            {
                os.write(buffer, 0, read);
                totalPumped += read;
            }
        }
        if (closeOnFinish)
        {
            try
            {
                is.close();
            } catch (IOException e)
            {
            }
            try
            {
                os.close();
            } catch (IOException e)
            {
            }
        }
        return totalPumped;
    }

    /**
     * Pumps a stream as per {@link IOUtilities#pumpStream(java.io.InputStream, java.io.OutputStream)} but does not
     * block.  A new thread is created which performs the pumping operation.  If an I/O error occurs while reading or
     * writing, it is silently consumed and an attempt is made to close both streams.
     *
     * @param is The {@link InputStream} containing data.
     * @param os The {@link OutputStream} to receive the data.
     */
    public static void pumpStreamWithoutBlocking(final InputStream is, final OutputStream os)
    {
        pumpStreamWithoutBlocking(is, os, 16384);
    }

    /**
     * Pumps a stream as per {@link IOUtilities#pumpStream(java.io.InputStream, java.io.OutputStream)} but does not
     * block.  A new thread is created which performs the pumping operation.  If an I/O error occurs while reading or
     * writing, it is silently consumed and an attempt is made to close both streams.
     *
     * @param is         The {@link InputStream} containing data.
     * @param os         The {@link OutputStream} to receive the data.
     * @param bufferSize The size of the buffer to use when pumping the stream.
     */
    public static void pumpStreamWithoutBlocking(final InputStream is, final OutputStream os, final int bufferSize)
    {
        pumpStreamWithoutBlocking(is, os, bufferSize, null, false);
    }

    /**
     * Pumps a stream as per {@link IOUtilities#pumpStream(java.io.InputStream, java.io.OutputStream)} but does not
     * block.  A new thread is created which performs the pumping operation.  If an I/O error occurs while reading or
     * writing, it is silently consumed and an attempt is made to close both streams.
     *
     * @param is            The {@link InputStream} containing data.
     * @param os            The {@link OutputStream} to receive the data.
     * @param bufferSize    The size of the buffer to use when pumping the stream.
     * @param maxBytes      The maximum number of bytes to pump, or <code>null</code> if no maximum should be applied.
     * @param closeOnFinish <code>true</code> if the streams should be closed when pumping is complete;
     *                      <code>false</code> if they should not.  Note that the streams are closed upon completion
     *                      even if the {@link InputStream} is not exhausted (as a result of not reading
     *                      <code>maxBytes</code> bytes).
     */
    public static void pumpStreamWithoutBlocking(final InputStream is, final OutputStream os, final int bufferSize,
                                                 final Long maxBytes, final boolean closeOnFinish)
    {
        new Thread("Stream pumping thread")
        {
            public void run()
            {
                try
                {
                    pumpStream(is, os, bufferSize, maxBytes, closeOnFinish);
                } catch (IOException e)
                {
                    try
                    {
                        is.close();
                    } catch (IOException ioe)
                    {
                    }
                    try
                    {
                        os.close();
                    } catch (IOException ioe)
                    {
                    }
                }
            }
        }.start();
    }

    /**
     * Reads data from the provided {@link InputStream} until the provided array is filled or the stream is exhausted.
     *
     * @param is   The {@link InputStream} from which to read.
     * @param data The array into which data should be read.
     * @throws IOException  If the {@link InputStream} throws an {@link IOException} during reading.
     * @throws EOFException If the stream is exhausted before the array is filled.
     */
    public static void readFully(InputStream is, byte[] data)
            throws IOException, EOFException
    {
        int left = data.length;
        int offset = 0;
        while (left > 0)
        {
            int read = is.read(data, offset, left);
            if (read == -1) throw new EOFException("Unexpected end of stream.");
            left -= read;
            offset += read;
        }
    }
}

// END OF FILE
