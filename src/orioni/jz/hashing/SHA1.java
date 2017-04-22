package orioni.jz.hashing;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class is an implementation of the SHA1 hashing algorithm.  Note that it is impossible for this class to encrypt
 * a bit string whose length is not a multiple of eight bits.
 *
 * @author Zachary Palmer
 */
public class SHA1 extends HashingAlgorithm<int[]>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * A singleton instance of this hashing algorithm.
     */
    public static final SHA1 SINGLETON = new SHA1();

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Private constructor.
     */
    private SHA1()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Performs the SHA-1 hashing algorithm on a set of input data provided through an {@link InputStream}.
     *
     * @param source The {@link InputStream} which will be used as a source for the data.  This {@link InputStream} will
     *               be read until it is exhausted.
     * @return An <code>int[5]</code> containing the SHA-1 hash, with the first byte of the message in offset
     *         <code>0</code>.
     * @throws IOException If an I/O error occurs while retrieving data from the provided stream.
     */
    public int[] hash(InputStream source)
            throws IOException
    {
        byte[] messageBlock = new byte[64];
        int[] registers = new int[]{0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0};
        int read = source.read(messageBlock);
        if (read == -1) read = 0;
        long lengthCounter = read;
        while (read == 64)
        {
            processOnePass(registers, messageBlock);
            read = source.read(messageBlock);
            if (read == -1) read = 0;
            lengthCounter += read;
        }
        // perform padding
        messageBlock[read] = (byte) (0x80);
        lengthCounter *= 8;
        if (read > 55)
        {
            // pad a whole message block
            for (int i = read + 1; i < messageBlock.length; i++) messageBlock[i] = 0;
            processOnePass(registers, messageBlock);
            // now create final message block
            for (int i = 0; i < messageBlock.length - 8; i++)
            {
                messageBlock[i] = 0;
            }
        } else
        {
            // pad final message block
            for (int i = read + 1; i < messageBlock.length - 8; i++) messageBlock[i] = 0;
        }
        // incorporate the length counter as a little-endian 64-bit integer
        for (int i = messageBlock.length - 1; i >= messageBlock.length - 8; i--)
        {
            messageBlock[i] = (byte) (lengthCounter % 256);
            lengthCounter /= 256;
        }
        processOnePass(registers, messageBlock);
        return registers;
    }

    /**
     * Processes one pass of the SHA-1 algorithm.  This is possible because passes are only dependent upon the values in
     * the return registers; the other data expires in usefulness at the end of each pass.
     *
     * @param registers    The five H-registers which contain the return data between passes.
     * @param messageBytes A <code>byte[64]</code> containing the message_bytes block to be used in this pass.
     */
    private static void processOnePass(int[] registers, byte[] messageBytes)
    {
        int a, b, c, d, e, t;
        int w0, w1, w2, w3, w4, w5, w6, w7, w8, w9, w10, w11, w12, w13, w14, w15, w16, w17, w18, w19,
                w20, w21, w22, w23, w24, w25, w26, w27, w28, w29, w30, w31, w32, w33, w34, w35, w36, w37, w38, w39,
                w40, w41, w42, w43, w44, w45, w46, w47, w48, w49, w50, w51, w52, w53, w54, w55, w56, w57, w58, w59,
                w60, w61, w62, w63, w64, w65, w66, w67, w68, w69, w70, w71, w72, w73, w74, w75, w76, w77, w78, w79;
        w0 = ((messageBytes[0] & 0xFF) << 24) | ((messageBytes[1] & 0xFF) << 16) |
             ((messageBytes[2] & 0xFF) << 8) | ((messageBytes[3] & 0xFF));
        w1 = ((messageBytes[4] & 0xFF) << 24) | ((messageBytes[5] & 0xFF) << 16) |
             ((messageBytes[6] & 0xFF) << 8) | ((messageBytes[7] & 0xFF));
        w2 = ((messageBytes[8] & 0xFF) << 24) | ((messageBytes[9] & 0xFF) << 16) |
             ((messageBytes[10] & 0xFF) << 8) | ((messageBytes[11] & 0xFF));
        w3 = ((messageBytes[12] & 0xFF) << 24) | ((messageBytes[13] & 0xFF) << 16) |
             ((messageBytes[14] & 0xFF) << 8) | ((messageBytes[15] & 0xFF));
        w4 = ((messageBytes[16] & 0xFF) << 24) | ((messageBytes[17] & 0xFF) << 16) |
             ((messageBytes[18] & 0xFF) << 8) | ((messageBytes[19] & 0xFF));
        w5 = ((messageBytes[20] & 0xFF) << 24) | ((messageBytes[21] & 0xFF) << 16) |
             ((messageBytes[22] & 0xFF) << 8) | ((messageBytes[23] & 0xFF));
        w6 = ((messageBytes[24] & 0xFF) << 24) | ((messageBytes[25] & 0xFF) << 16) |
             ((messageBytes[26] & 0xFF) << 8) | ((messageBytes[27] & 0xFF));
        w7 = ((messageBytes[28] & 0xFF) << 24) | ((messageBytes[29] & 0xFF) << 16) |
             ((messageBytes[30] & 0xFF) << 8) | ((messageBytes[31] & 0xFF));
        w8 = ((messageBytes[32] & 0xFF) << 24) | ((messageBytes[33] & 0xFF) << 16) |
             ((messageBytes[34] & 0xFF) << 8) | ((messageBytes[35] & 0xFF));
        w9 = ((messageBytes[36] & 0xFF) << 24) | ((messageBytes[37] & 0xFF) << 16) |
             ((messageBytes[38] & 0xFF) << 8) | ((messageBytes[39] & 0xFF));
        w10 = ((messageBytes[40] & 0xFF) << 24) | ((messageBytes[41] & 0xFF) << 16) |
              ((messageBytes[42] & 0xFF) << 8) | ((messageBytes[43] & 0xFF));
        w11 = ((messageBytes[44] & 0xFF) << 24) | ((messageBytes[45] & 0xFF) << 16) |
              ((messageBytes[46] & 0xFF) << 8) | ((messageBytes[47] & 0xFF));
        w12 = ((messageBytes[48] & 0xFF) << 24) | ((messageBytes[49] & 0xFF) << 16) |
              ((messageBytes[50] & 0xFF) << 8) | ((messageBytes[51] & 0xFF));
        w13 = ((messageBytes[52] & 0xFF) << 24) | ((messageBytes[53] & 0xFF) << 16) |
              ((messageBytes[54] & 0xFF) << 8) | ((messageBytes[55] & 0xFF));
        w14 = ((messageBytes[56] & 0xFF) << 24) | ((messageBytes[57] & 0xFF) << 16) |
              ((messageBytes[58] & 0xFF) << 8) | ((messageBytes[59] & 0xFF));
        w15 = ((messageBytes[60] & 0xFF) << 24) | ((messageBytes[61] & 0xFF) << 16) |
              ((messageBytes[62] & 0xFF) << 8) | ((messageBytes[63] & 0xFF));
        t = w13 ^ w8 ^ w2 ^ w0;
        w16 = (t << 1) | (t >>> 31);
        t = w14 ^ w9 ^ w3 ^ w1;
        w17 = (t << 1) | (t >>> 31);
        t = w15 ^ w10 ^ w4 ^ w2;
        w18 = (t << 1) | (t >>> 31);
        t = w16 ^ w11 ^ w5 ^ w3;
        w19 = (t << 1) | (t >>> 31);
        t = w17 ^ w12 ^ w6 ^ w4;
        w20 = (t << 1) | (t >>> 31);
        t = w18 ^ w13 ^ w7 ^ w5;
        w21 = (t << 1) | (t >>> 31);
        t = w19 ^ w14 ^ w8 ^ w6;
        w22 = (t << 1) | (t >>> 31);
        t = w20 ^ w15 ^ w9 ^ w7;
        w23 = (t << 1) | (t >>> 31);
        t = w21 ^ w16 ^ w10 ^ w8;
        w24 = (t << 1) | (t >>> 31);
        t = w22 ^ w17 ^ w11 ^ w9;
        w25 = (t << 1) | (t >>> 31);
        t = w23 ^ w18 ^ w12 ^ w10;
        w26 = (t << 1) | (t >>> 31);
        t = w24 ^ w19 ^ w13 ^ w11;
        w27 = (t << 1) | (t >>> 31);
        t = w25 ^ w20 ^ w14 ^ w12;
        w28 = (t << 1) | (t >>> 31);
        t = w26 ^ w21 ^ w15 ^ w13;
        w29 = (t << 1) | (t >>> 31);
        t = w27 ^ w22 ^ w16 ^ w14;
        w30 = (t << 1) | (t >>> 31);
        t = w28 ^ w23 ^ w17 ^ w15;
        w31 = (t << 1) | (t >>> 31);
        t = w29 ^ w24 ^ w18 ^ w16;
        w32 = (t << 1) | (t >>> 31);
        t = w30 ^ w25 ^ w19 ^ w17;
        w33 = (t << 1) | (t >>> 31);
        t = w31 ^ w26 ^ w20 ^ w18;
        w34 = (t << 1) | (t >>> 31);
        t = w32 ^ w27 ^ w21 ^ w19;
        w35 = (t << 1) | (t >>> 31);
        t = w33 ^ w28 ^ w22 ^ w20;
        w36 = (t << 1) | (t >>> 31);
        t = w34 ^ w29 ^ w23 ^ w21;
        w37 = (t << 1) | (t >>> 31);
        t = w35 ^ w30 ^ w24 ^ w22;
        w38 = (t << 1) | (t >>> 31);
        t = w36 ^ w31 ^ w25 ^ w23;
        w39 = (t << 1) | (t >>> 31);
        t = w37 ^ w32 ^ w26 ^ w24;
        w40 = (t << 1) | (t >>> 31);
        t = w38 ^ w33 ^ w27 ^ w25;
        w41 = (t << 1) | (t >>> 31);
        t = w39 ^ w34 ^ w28 ^ w26;
        w42 = (t << 1) | (t >>> 31);
        t = w40 ^ w35 ^ w29 ^ w27;
        w43 = (t << 1) | (t >>> 31);
        t = w41 ^ w36 ^ w30 ^ w28;
        w44 = (t << 1) | (t >>> 31);
        t = w42 ^ w37 ^ w31 ^ w29;
        w45 = (t << 1) | (t >>> 31);
        t = w43 ^ w38 ^ w32 ^ w30;
        w46 = (t << 1) | (t >>> 31);
        t = w44 ^ w39 ^ w33 ^ w31;
        w47 = (t << 1) | (t >>> 31);
        t = w45 ^ w40 ^ w34 ^ w32;
        w48 = (t << 1) | (t >>> 31);
        t = w46 ^ w41 ^ w35 ^ w33;
        w49 = (t << 1) | (t >>> 31);
        t = w47 ^ w42 ^ w36 ^ w34;
        w50 = (t << 1) | (t >>> 31);
        t = w48 ^ w43 ^ w37 ^ w35;
        w51 = (t << 1) | (t >>> 31);
        t = w49 ^ w44 ^ w38 ^ w36;
        w52 = (t << 1) | (t >>> 31);
        t = w50 ^ w45 ^ w39 ^ w37;
        w53 = (t << 1) | (t >>> 31);
        t = w51 ^ w46 ^ w40 ^ w38;
        w54 = (t << 1) | (t >>> 31);
        t = w52 ^ w47 ^ w41 ^ w39;
        w55 = (t << 1) | (t >>> 31);
        t = w53 ^ w48 ^ w42 ^ w40;
        w56 = (t << 1) | (t >>> 31);
        t = w54 ^ w49 ^ w43 ^ w41;
        w57 = (t << 1) | (t >>> 31);
        t = w55 ^ w50 ^ w44 ^ w42;
        w58 = (t << 1) | (t >>> 31);
        t = w56 ^ w51 ^ w45 ^ w43;
        w59 = (t << 1) | (t >>> 31);
        t = w57 ^ w52 ^ w46 ^ w44;
        w60 = (t << 1) | (t >>> 31);
        t = w58 ^ w53 ^ w47 ^ w45;
        w61 = (t << 1) | (t >>> 31);
        t = w59 ^ w54 ^ w48 ^ w46;
        w62 = (t << 1) | (t >>> 31);
        t = w60 ^ w55 ^ w49 ^ w47;
        w63 = (t << 1) | (t >>> 31);
        t = w61 ^ w56 ^ w50 ^ w48;
        w64 = (t << 1) | (t >>> 31);
        t = w62 ^ w57 ^ w51 ^ w49;
        w65 = (t << 1) | (t >>> 31);
        t = w63 ^ w58 ^ w52 ^ w50;
        w66 = (t << 1) | (t >>> 31);
        t = w64 ^ w59 ^ w53 ^ w51;
        w67 = (t << 1) | (t >>> 31);
        t = w65 ^ w60 ^ w54 ^ w52;
        w68 = (t << 1) | (t >>> 31);
        t = w66 ^ w61 ^ w55 ^ w53;
        w69 = (t << 1) | (t >>> 31);
        t = w67 ^ w62 ^ w56 ^ w54;
        w70 = (t << 1) | (t >>> 31);
        t = w68 ^ w63 ^ w57 ^ w55;
        w71 = (t << 1) | (t >>> 31);
        t = w69 ^ w64 ^ w58 ^ w56;
        w72 = (t << 1) | (t >>> 31);
        t = w70 ^ w65 ^ w59 ^ w57;
        w73 = (t << 1) | (t >>> 31);
        t = w71 ^ w66 ^ w60 ^ w58;
        w74 = (t << 1) | (t >>> 31);
        t = w72 ^ w67 ^ w61 ^ w59;
        w75 = (t << 1) | (t >>> 31);
        t = w73 ^ w68 ^ w62 ^ w60;
        w76 = (t << 1) | (t >>> 31);
        t = w74 ^ w69 ^ w63 ^ w61;
        w77 = (t << 1) | (t >>> 31);
        t = w75 ^ w70 ^ w64 ^ w62;
        w78 = (t << 1) | (t >>> 31);
        t = w76 ^ w71 ^ w65 ^ w63;
        w79 = (t << 1) | (t >>> 31);

        a = registers[0];
        b = registers[1];
        c = registers[2];
        d = registers[3];
        e = registers[4];
        t = ((a << 5) | (a >>> 27)) + ((b & c) | ((~b) & d)) + e + w0 + 0x5A827999;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | ((~b) & d)) + e + w1 + 0x5A827999;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | ((~b) & d)) + e + w2 + 0x5A827999;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | ((~b) & d)) + e + w3 + 0x5A827999;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | ((~b) & d)) + e + w4 + 0x5A827999;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | ((~b) & d)) + e + w5 + 0x5A827999;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | ((~b) & d)) + e + w6 + 0x5A827999;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | ((~b) & d)) + e + w7 + 0x5A827999;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | ((~b) & d)) + e + w8 + 0x5A827999;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | ((~b) & d)) + e + w9 + 0x5A827999;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | ((~b) & d)) + e + w10 + 0x5A827999;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | ((~b) & d)) + e + w11 + 0x5A827999;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | ((~b) & d)) + e + w12 + 0x5A827999;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | ((~b) & d)) + e + w13 + 0x5A827999;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | ((~b) & d)) + e + w14 + 0x5A827999;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | ((~b) & d)) + e + w15 + 0x5A827999;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | ((~b) & d)) + e + w16 + 0x5A827999;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | ((~b) & d)) + e + w17 + 0x5A827999;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | ((~b) & d)) + e + w18 + 0x5A827999;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | ((~b) & d)) + e + w19 + 0x5A827999;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w20 + 0x6ed9ebA1;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w21 + 0x6ed9ebA1;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w22 + 0x6ed9ebA1;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w23 + 0x6ed9ebA1;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w24 + 0x6ed9ebA1;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w25 + 0x6ed9ebA1;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w26 + 0x6ed9ebA1;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w27 + 0x6ed9ebA1;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w28 + 0x6ed9ebA1;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w29 + 0x6ed9ebA1;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w30 + 0x6ed9ebA1;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w31 + 0x6ed9ebA1;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w32 + 0x6ed9ebA1;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w33 + 0x6ed9ebA1;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w34 + 0x6ed9ebA1;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w35 + 0x6ed9ebA1;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w36 + 0x6ed9ebA1;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w37 + 0x6ed9ebA1;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w38 + 0x6ed9ebA1;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w39 + 0x6ed9ebA1;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | (b & d) | (c & d)) + e + w40 + 0x8F1bbcdc;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | (b & d) | (c & d)) + e + w41 + 0x8F1bbcdc;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | (b & d) | (c & d)) + e + w42 + 0x8F1bbcdc;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | (b & d) | (c & d)) + e + w43 + 0x8F1bbcdc;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | (b & d) | (c & d)) + e + w44 + 0x8F1bbcdc;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | (b & d) | (c & d)) + e + w45 + 0x8F1bbcdc;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | (b & d) | (c & d)) + e + w46 + 0x8F1bbcdc;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | (b & d) | (c & d)) + e + w47 + 0x8F1bbcdc;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | (b & d) | (c & d)) + e + w48 + 0x8F1bbcdc;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | (b & d) | (c & d)) + e + w49 + 0x8F1bbcdc;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | (b & d) | (c & d)) + e + w50 + 0x8F1bbcdc;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | (b & d) | (c & d)) + e + w51 + 0x8F1bbcdc;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | (b & d) | (c & d)) + e + w52 + 0x8F1bbcdc;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | (b & d) | (c & d)) + e + w53 + 0x8F1bbcdc;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | (b & d) | (c & d)) + e + w54 + 0x8F1bbcdc;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | (b & d) | (c & d)) + e + w55 + 0x8F1bbcdc;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | (b & d) | (c & d)) + e + w56 + 0x8F1bbcdc;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | (b & d) | (c & d)) + e + w57 + 0x8F1bbcdc;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | (b & d) | (c & d)) + e + w58 + 0x8F1bbcdc;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + ((b & c) | (b & d) | (c & d)) + e + w59 + 0x8F1bbcdc;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w60 + 0xcA62c1d6;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w61 + 0xcA62c1d6;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w62 + 0xcA62c1d6;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w63 + 0xcA62c1d6;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w64 + 0xcA62c1d6;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w65 + 0xcA62c1d6;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w66 + 0xcA62c1d6;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w67 + 0xcA62c1d6;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w68 + 0xcA62c1d6;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w69 + 0xcA62c1d6;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w70 + 0xcA62c1d6;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w71 + 0xcA62c1d6;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w72 + 0xcA62c1d6;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w73 + 0xcA62c1d6;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w74 + 0xcA62c1d6;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w75 + 0xcA62c1d6;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w76 + 0xcA62c1d6;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w77 + 0xcA62c1d6;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w78 + 0xcA62c1d6;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        t = ((a << 5) | (a >>> 27)) + (b ^ c ^ d) + e + w79 + 0xcA62c1d6;
        e = d;
        d = c;
        c = (b << 30) | (b >>> 2);
        b = a;
        a = t;
        registers[0] += a;
        registers[1] += b;
        registers[2] += c;
        registers[3] += d;
        registers[4] += e;
    }
}