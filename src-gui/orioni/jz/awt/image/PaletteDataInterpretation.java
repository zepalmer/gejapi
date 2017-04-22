package orioni.jz.awt.image;

/**
 * This <code>enum</code> type is meant to be used with the {@link orioni.jz.awt.image.ImageUtilities#interpretIndexColorModel(byte[],
        * orioni.jz.awt.image.PaletteDataInterpretation)} method.  It specifies a manner in which a <code>byte[]</code> should
 * be interpreted as palette data.
 */
public enum PaletteDataInterpretation
{
    /**
     * This interpretation method assumes that three consecutive bytes represent one palette entry.  The first byte is
     * the red register, the second is the green register, and the third is the blue register.
     */
    RGB,
    /**
     * This interpretation method assumes that three consecutive bytes represent one palette entry.  The first byte is
     * the blue register, the second is the green register, and the third is the red register.
     */
    BGR,
    /**
     * This interpretation method assumes that a palette entry is positioned throughout the array.  The red register
     * appears at the offset <code>color_index</code>, the green at the offset <code>color_index + (length / 3)</code>,
     * and the blue at the offset <code>color_index + (length / 3) * 2</code>.
     */
    RGB_BURST,
    /**
     * This interpretation method assumes that a palette entry is positioned throughout the array.  The blue register
     * appears at the offset <code>color_index</code>, the green at the offset <code>color_index + (length / 3)</code>,
     * and the red at the offset <code>color_index + (length / 3) * 2</code>.
     */
    BGR_BURST
}

// END OF FILE