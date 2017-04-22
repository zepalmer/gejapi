package orioni.jz.net;

import java.io.*;
import java.net.Socket;

/**
 * This class is designed to operate over a {@link Socket} by wrapping the {@link Socket}'s {@link InputStream} and
 * {@link OutputStream} in an {@link ObjectInputStream} and an {@link ObjectOutputStream}, respectively.
 *
 * @author Zachary Palmer
 */
public class ObjectSocket
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link Socket} over which this {@link ObjectSocket} should wrap.
     */
    protected Socket socket;
    /**
     * The {@link ObjectInputStream} which wraps the {@link Socket}'s {@link InputStream}.
     */
    protected ObjectInputStream objectInputStream;
    /**
     * The {@link ObjectOutputStream} which wraps the {@link Socket}'s {@link OutputStream}.
     */
    protected ObjectOutputStream objectOutputStream;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Attempts to initialize both streams simultaneously.  This constructor is easier to use
     * than the constructors while attempt to initialize the streams in serial, but is more demainding on processing
     * resources.
     *
     * @param socket The {@link Socket} over which this {@link ObjectSocket} should wrap.
     * @throws IOException If an I/O error occurs while trying to create the object streams.
     */
    public ObjectSocket(Socket socket)
            throws IOException
    {
        super();
        this.socket = socket;
        objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
        objectInputStream = new ObjectInputStream(this.socket.getInputStream());
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the {@link ObjectInputStream} that this {@link ObjectSocket} created.
     *
     * @return The {@link ObjectInputStream} that this {@link ObjectSocket} created.
     */
    public ObjectInputStream getObjectInputStream()
    {
        return objectInputStream;
    }

    /**
     * Retrieves the {@link ObjectOutputStream} that this {@link ObjectSocket} created.
     *
     * @return The {@link ObjectOutputStream} that this {@link ObjectSocket} created.
     */
    public ObjectOutputStream getObjectOutputStream()
    {
        return objectOutputStream;
    }

    /**
     * Retrieves the {@link Socket} that this {@link ObjectSocket} wrapped.
     *
     * @return The {@link Socket} that this {@link ObjectSocket} wrapped.
     */
    public Socket getSocket()
    {
        return socket;
    }

    /**
     * Closes this {@link ObjectSocket}.  This is accomplished by flushing and closing the object streams and closing
     * the underlying socket.
     *
     * @throws IOException If an I/O error occurred while attempting this close operation.
     */
    public void close()
            throws IOException
    {
        objectInputStream.close();
        objectOutputStream.close();
        socket.getOutputStream().flush();
        socket.close();
    }

    /**
     * Closes this {@link ObjectSocket}.  The object streams are flushed and closed and the underlying socket is closed.
     * All exceptions are completely ignored.  Each task is performed without regard to previous exceptions; that is, if
     * an exception occurs while closing an object stream, a closing call to the underlying socket will still be made.
     *
     * @return A {@link String} containing stack traces from any exceptions which may have occurred.
     */
    public String closeCold()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        try
        {
            objectInputStream.close();
        } catch (IOException e)
        {
            e.printStackTrace(ps);
        }

        try
        {
            objectOutputStream.close();
        } catch (IOException e)
        {
            e.printStackTrace(ps);
        }

        try
        {
            socket.close();
        } catch (IOException e)
        {
            e.printStackTrace(ps);
        }

        ps.close();
        return baos.toString();
    }

    /**
     * Produces a {@link String} which describes this message.
     *
     * @return A {@link String} which describes this message.
     */
    public String toString()
    {
        return "Object Socket " + socket.getInetAddress() + ":" + socket.getPort();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE