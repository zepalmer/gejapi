package orioni.jz.io.bit;

/**
 * This enumeration is used to define the order in which bits are stored in bytes.  It is not intended as a storage
 * order for individual bits themselves but as an indication of which bits are first to be used when storing data.  For
 * example, consider the process of storing a number of four-bit values in a byte array.  Described as an array, the
 * four-bit values may be <code>{0x7, 0xD, 0x6, 0x1, 0xF, 0x5}</code>.
 * <p/>
 * For space saving purposes, however, the four bit values may be packed into bytes, each byte containing two values. In
 * this example, <code>0x7</code> and <code>0xD</code> would be stored in the same byte in the resulting array.  The
 * question arises: should the byte be stored as <code>0x7D</code> or <code>0xD7</code>?  That is, should the first
 * four-bit value stored in the array be stored in the higher bits of its byte or the lower bits?  The {@link BitOrder}
 * enumeration addresses this question; the {@link BitOrder#HIGHEST_BIT_FIRST} value would indicate that the byte should
 * be stored as <code>0x7D</code>, while the {@link BitOrder#LOWEST_BIT_FIRST} value would indicate that the byte should
 * be stored as <code>0xD7</code>.
 * <p/>
 * Likewise, this approach works for values which cross byte boundaries.  Consider the case of eight three-bit values
 * stored in a three byte array in big-endian format.  For purposes of description, the three bit values will be
 * lettered <code>a</code> through <code>h</code> and the most significant bit of a value will be subscripted with a
 * <code>2</code>, while the least significant bit will be subscripted with a <code>0</code>.  In this example, the
 * following is true: <ul> <li>If the byte array is stored {@link BitOrder#HIGHEST_BIT_FIRST}, the byte array will
 * be<br> <code>{a<sub>2</sub>a<sub>1</sub>a<sub>0</sub>b<sub>2</sub>b<sub>1</sub>b<sub>0</sub>c<sub>2</sub>c<sub>1</sub>,
 * c<sub>0</sub>d<sub>2</sub>d<sub>1</sub>d<sub>0</sub>e<sub>2</sub>e<sub>1</sub>e<sub>0</sub>f<sub>2</sub>,
 * f<sub>1</sub>f<sub>0</sub>g<sub>2</sub>g<sub>1</sub>g<sub>0</sub>h<sub>2</sub>h<sub>1</sub>h<sub>0</sub>}</code>.
 * </li> <li>If the byte array is stored {@link BitOrder#LOWEST_BIT_FIRST}, the byte array will be<br>
 * <code>{c<sub>1</sub>c<sub>0</sub>b<sub>2</sub>b<sub>1</sub>b<sub>0</sub>a<sub>2</sub>a<sub>1</sub>a<sub>0</sub>,
 * f<sub>0</sub>e<sub>2</sub>e<sub>1</sub>e<sub>0</sub>d<sub>2</sub>d<sub>1</sub>d<sub>0</sub>c<sub>2</sub>,
 * h<sub>2</sub>h<sub>1</sub>h<sub>0</sub>g<sub>2</sub>g<sub>1</sub>g<sub>0</sub>f<sub>2</sub>f<sub>1</sub>}</code>.
 * </li> </ul>
 *
 * @author Zachary Palmer
 */
public enum BitOrder
{
    /**
     * This {@link BitOrder} is designed to specify that the highest bit is used first.  For example, the byte
     * <code>0x7D</code> contains two four bit values: <code>0x7</code> and <code>0xD</code>, in that order.
     */
    HIGHEST_BIT_FIRST,
    /**
     * This {@link BitOrder} is designed to specify that the lowest bit is used first.  For example, the byte
     * <code>0x7D</code> contains two four bit values: <code>0xD</code> and <code>0x7</code>, in that order.
     */
    LOWEST_BIT_FIRST
}

// END OF FILE