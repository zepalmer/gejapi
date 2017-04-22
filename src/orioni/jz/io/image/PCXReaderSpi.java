package orioni.jz.io.image;

import orioni.jz.common.exception.ParseException;

import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.IIORegistry;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.util.Locale;

/**
 * The service provider instance for {@link PCXReader}.
 *
 * @author Zachary Palmer
 */
public class PCXReaderSpi extends ImageReaderSpi
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// STATIC INITIALIZER ////////////////////////////////////////////////////////////

    static
    {
        register();
    }

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public PCXReaderSpi()
    {
        super("Orion Innovations", "beta", new String[]{"pcx"}, new String[]{"pcx"}, new String[]{},
              PCXReader.class.getName(), new Class[]{ImageInputStream.class},
              new String[]{PCXWriterSpi.class.getName()}, false, null, null, null, null, false,
              null, null, null, null);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Attempts to determine if the provided information source contains a {@link PCXHeader}.
     *
     * @param source the object (typically an <code>ImageInputStream</code>) to be decoded.
     * @return <code>true</code> if it is likely that this stream can be decoded.
     * @throws IllegalArgumentException if <code>source</code> is <code>null</code>.
     * @throws java.io.IOException      if an I/O error occurs while reading the stream.
     */
    public boolean canDecodeInput(Object source)
            throws IOException
    {
        if (source==null)
        {
            throw new IllegalArgumentException("Null input source.");
        }
        ImageInputStream iis;
        try
        {
            iis = (ImageInputStream)source;
        } catch (ClassCastException e)
        {
            return false;
        }
        iis.mark();
        boolean success;
        try
        {
            new PCXHeader(iis);
            success = true;
        } catch (ParseException e)
        {
            success = false;
        }
        iis.reset();
        return success;
    }

    /**
     * Returns an instance of the <code>ImageReader</code> implementation associated with this service provider. The
     * returned object will initially be in an initial state as if its <code>reset</code> method had been called.
     * <p/>
     * <p> An <code>Object</code> may be supplied to the plug-in at construction time.  The nature of the object is
     * entirely plug-in specific.
     * <p/>
     * <p> Typically, a plug-in will implement this method using code such as <code>return new
     * MyImageReader(this)</code>.
     *
     * @param extension a plug-in specific extension object, which may be <code>null</code>.
     * @return an <code>ImageReader</code> instance.
     * @throws java.io.IOException      if the attempt to instantiate the reader fails.
     * @throws IllegalArgumentException if the <code>ImageReader</code>'s contructor throws an
     *                                  <code>IllegalArgumentException</code> to indicate that the extension object is
     *                                  unsuitable.
     */
    public ImageReader createReaderInstance(Object extension)
            throws IOException
    {
        return new PCXReader(this);
    }

    /**
     * Returns a brief, human-readable description of this service provider and its associated implementation.  The
     * resulting string should be localized for the supplied <code>Locale</code>, if possible.
     *
     * @param locale a <code>Locale</code> for which the return value should be localized.
     * @return a <code>String</code> containing a description of this service provider.
     */
    public String getDescription(Locale locale)
    {
        return "JZCode PCX image reader";
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Attempts to register an instance of this SPI with the {@link IIORegistry}.
     */
    public static void register()
    {
        IIORegistry.getDefaultInstance().registerServiceProvider(new PCXReaderSpi());
    }
}

// END OF FILE