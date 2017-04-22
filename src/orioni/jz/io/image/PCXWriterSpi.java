package orioni.jz.io.image;

import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.spi.IIORegistry;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import java.io.IOException;
import java.util.Locale;

/**
 * <B><I>This file lacks a description.</I></B>
 *
 * @author Zachary Palmer
 */
public class PCXWriterSpi extends ImageWriterSpi
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
    public PCXWriterSpi()
    {
        super(
                "Orion Innovations", "beta", new String[]{"pcx"}, new String[]{"pcx"}, new String[]{},
                PCXWriter.class.getName(), new Class[]{ImageOutputStream.class},
                new String[]{PCXReaderSpi.class.getName()}, false, null, null, null, null, false,
                null, null, null, null);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Returns <code>true</code>.  Virtually any RGB image can be encoded into PCX format, albeit not perfectly.
     *
     * @param type Ignored.
     * @return <code>true</code>, always.
     */
    public boolean canEncodeImage(ImageTypeSpecifier type)
    {
        return true;
    }

    /**
     * Returns an instance of {@link PCXWriter}.
     *
     * @param extension Ignored.
     * @return A {@link PCXWriter}.
     * @throws java.io.IOException      If the attempt to instantiate the writer fails.
     */
    public ImageWriter createWriterInstance(Object extension)
            throws IOException
    {
        return new PCXWriter(this);
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
        return "JZCode PCX image writer";
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Attempts to register an instance of this SPI with the {@link javax.imageio.spi.IIORegistry}.
     */
    public static void register()
    {
        IIORegistry.getDefaultInstance().registerServiceProvider(new PCXWriterSpi());
    }
}

// END OF FILE