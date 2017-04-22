package orioni.jz.cryptography;

import orioni.jz.util.DataConversion;
import orioni.jz.util.Utilities;

/**
 * This class is an implementation of the Blowfish algorithm, a symmetric block cipher which uses blocks of 64 bits in
 * length.
 *
 * @author Zachary Palmer
 */
public final class Blowfish
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * The initial values for the first S-box, according to the Blowfish specification.
     */
    private static final int[] INITIAL_S_BOX_1 = new int[]{
        0xd1310ba6, 0x98dfb5ac, 0x2ffd72db, 0xd01adfb7, 0xb8e1afed, 0x6a267e96, 0xba7c9045, 0xf12c7f99,
        0x24a19947, 0xb3916cf7, 0x0801f2e2, 0x858efc16, 0x636920d8, 0x71574e69, 0xa458fea3, 0xf4933d7e,
        0x0d95748f, 0x728eb658, 0x718bcd58, 0x82154aee, 0x7b54a41d, 0xc25a59b5, 0x9c30d539, 0x2af26013,
        0xc5d1b023, 0x286085f0, 0xca417918, 0xb8db38ef, 0x8e79dcb0, 0x603a180e, 0x6c9e0e8b, 0xb01e8a3e,
        0xd71577c1, 0xbd314b27, 0x78af2fda, 0x55605c60, 0xe65525f3, 0xaa55ab94, 0x57489862, 0x63e81440,
        0x55ca396a, 0x2aab10b6, 0xb4cc5c34, 0x1141e8ce, 0xa15486af, 0x7c72e993, 0xb3ee1411, 0x636fbc2a,
        0x2ba9c55d, 0x741831f6, 0xce5c3e16, 0x9b87931e, 0xafd6ba33, 0x6c24cf5c, 0x7a325381, 0x28958677,
        0x3b8f4898, 0x6b4bb9af, 0xc4bfe81b, 0x66282193, 0x61d809cc, 0xfb21a991, 0x487cac60, 0x5dec8032,
        0xef845d5d, 0xe98575b1, 0xdc262302, 0xeb651b88, 0x23893e81, 0xd396acc5, 0x0f6d6ff3, 0x83f44239,
        0x2e0b4482, 0xa4842004, 0x69c8f04a, 0x9e1f9b5e, 0x21c66842, 0xf6e96c9a, 0x670c9c61, 0xabd388f0,
        0x6a51a0d2, 0xd8542f68, 0x960fa728, 0xab5133a3, 0x6eef0b6c, 0x137a3be4, 0xba3bf050, 0x7efb2a98,
        0xa1f1651d, 0x39af0176, 0x66ca593e, 0x82430e88, 0x8cee8619, 0x456f9fb4, 0x7d84a5c3, 0x3b8b5ebe,
        0xe06f75d8, 0x85c12073, 0x401a449f, 0x56c16aa6, 0x4ed3aa62, 0x363f7706, 0x1bfedf72, 0x429b023d,
        0x37d0d724, 0xd00a1248, 0xdb0fead3, 0x49f1c09b, 0x075372c9, 0x80991b7b, 0x25d479d8, 0xf6e8def7,
        0xe3fe501a, 0xb6794c3b, 0x976ce0bd, 0x04c006ba, 0xc1a94fb6, 0x409f60c4, 0x5e5c9ec2, 0x196a2463,
        0x68fb6faf, 0x3e6c53b5, 0x1339b2eb, 0x3b52ec6f, 0x6dfc511f, 0x9b30952c, 0xcc814544, 0xaf5ebd09,
        0xbee3d004, 0xde334afd, 0x660f2807, 0x192e4bb3, 0xc0cba857, 0x45c8740f, 0xd20b5f39, 0xb9d3fbdb,
        0x5579c0bd, 0x1a60320a, 0xd6a100c6, 0x402c7279, 0x679f25fe, 0xfb1fa3cc, 0x8ea5e9f8, 0xdb3222f8,
        0x3c7516df, 0xfd616b15, 0x2f501ec8, 0xad0552ab, 0x323db5fa, 0xfd238760, 0x53317b48, 0x3e00df82,
        0x9e5c57bb, 0xca6f8ca0, 0x1a87562e, 0xdf1769db, 0xd542a8f6, 0x287effc3, 0xac6732c6, 0x8c4f5573,
        0x695b27b0, 0xbbca58c8, 0xe1ffa35d, 0xb8f011a0, 0x10fa3d98, 0xfd2183b8, 0x4afcb56c, 0x2dd1d35b,
        0x9a53e479, 0xb6f84565, 0xd28e49bc, 0x4bfb9790, 0xe1ddf2da, 0xa4cb7e33, 0x62fb1341, 0xcee4c6e8,
        0xef20cada, 0x36774c01, 0xd07e9efe, 0x2bf11fb4, 0x95dbda4d, 0xae909198, 0xeaad8e71, 0x6b93d5a0,
        0xd08ed1d0, 0xafc725e0, 0x8e3c5b2f, 0x8e7594b7, 0x8ff6e2fb, 0xf2122b64, 0x8888b812, 0x900df01c,
        0x4fad5ea0, 0x688fc31c, 0xd1cff191, 0xb3a8c1ad, 0x2f2f2218, 0xbe0e1777, 0xea752dfe, 0x8b021fa1,
        0xe5a0cc0f, 0xb56f74e8, 0x18acf3d6, 0xce89e299, 0xb4a84fe0, 0xfd13e0b7, 0x7cc43b81, 0xd2ada8d9,
        0x165fa266, 0x80957705, 0x93cc7314, 0x211a1477, 0xe6ad2065, 0x77b5fa86, 0xc75442f5, 0xfb9d35cf,
        0xebcdaf0c, 0x7b3e89a0, 0xd6411bd3, 0xae1e7e49, 0x00250e2d, 0x2071b35e, 0x226800bb, 0x57b8e0af,
        0x2464369b, 0xf009b91e, 0x5563911d, 0x59dfa6aa, 0x78c14389, 0xd95a537f, 0x207d5ba2, 0x02e5b9c5,
        0x83260376, 0x6295cfa9, 0x11c81968, 0x4e734a41, 0xb3472dca, 0x7b14a94a, 0x1b510052, 0x9a532915,
        0xd60f573f, 0xbc9bc6e4, 0x2b60a476, 0x81e67400, 0x08ba6fb5, 0x571be91f, 0xf296ec6b, 0x2a0dd915,
        0xb6636521, 0xe7b9f9b6, 0xff34052e, 0xc5855664, 0x53b02d5d, 0xa99f8fa1, 0x08ba4799, 0x6e85076a
    };
    /**
     * The initial values for the second S-box, according to the Blowfish specification.
     */
    private static final int[] INITIAL_S_BOX_2 = new int[]{
        0x4b7a70e9, 0xb5b32944, 0xdb75092e, 0xc4192623, 0xad6ea6b0, 0x49a7df7d, 0x9cee60b8, 0x8fedb266,
        0xecaa8c71, 0x699a17ff, 0x5664526c, 0xc2b19ee1, 0x193602a5, 0x75094c29, 0xa0591340, 0xe4183a3e,
        0x3f54989a, 0x5b429d65, 0x6b8fe4d6, 0x99f73fd6, 0xa1d29c07, 0xefe830f5, 0x4d2d38e6, 0xf0255dc1,
        0x4cdd2086, 0x8470eb26, 0x6382e9c6, 0x021ecc5e, 0x09686b3f, 0x3ebaefc9, 0x3c971814, 0x6b6a70a1,
        0x687f3584, 0x52a0e286, 0xb79c5305, 0xaa500737, 0x3e07841c, 0x7fdeae5c, 0x8e7d44ec, 0x5716f2b8,
        0xb03ada37, 0xf0500c0d, 0xf01c1f04, 0x0200b3ff, 0xae0cf51a, 0x3cb574b2, 0x25837a58, 0xdc0921bd,
        0xd19113f9, 0x7ca92ff6, 0x94324773, 0x22f54701, 0x3ae5e581, 0x37c2dadc, 0xc8b57634, 0x9af3dda7,
        0xa9446146, 0x0fd0030e, 0xecc8c73e, 0xa4751e41, 0xe238cd99, 0x3bea0e2f, 0x3280bba1, 0x183eb331,
        0x4e548b38, 0x4f6db908, 0x6f420d03, 0xf60a04bf, 0x2cb81290, 0x24977c79, 0x5679b072, 0xbcaf89af,
        0xde9a771f, 0xd9930810, 0xb38bae12, 0xdccf3f2e, 0x5512721f, 0x2e6b7124, 0x501adde6, 0x9f84cd87,
        0x7a584718, 0x7408da17, 0xbc9f9abc, 0xe94b7d8c, 0xec7aec3a, 0xdb851dfa, 0x63094366, 0xc464c3d2,
        0xef1c1847, 0x3215d908, 0xdd433b37, 0x24c2ba16, 0x12a14d43, 0x2a65c451, 0x50940002, 0x133ae4dd,
        0x71dff89e, 0x10314e55, 0x81ac77d6, 0x5f11199b, 0x043556f1, 0xd7a3c76b, 0x3c11183b, 0x5924a509,
        0xf28fe6ed, 0x97f1fbfa, 0x9ebabf2c, 0x1e153c6e, 0x86e34570, 0xeae96fb1, 0x860e5e0a, 0x5a3e2ab3,
        0x771fe71c, 0x4e3d06fa, 0x2965dcb9, 0x99e71d0f, 0x803e89d6, 0x5266c825, 0x2e4cc978, 0x9c10b36a,
        0xc6150eba, 0x94e2ea78, 0xa5fc3c53, 0x1e0a2df4, 0xf2f74ea7, 0x361d2b3d, 0x1939260f, 0x19c27960,
        0x5223a708, 0xf71312b6, 0xebadfe6e, 0xeac31f66, 0xe3bc4595, 0xa67bc883, 0xb17f37d1, 0x018cff28,
        0xc332ddef, 0xbe6c5aa5, 0x65582185, 0x68ab9802, 0xeecea50f, 0xdb2f953b, 0x2aef7dad, 0x5b6e2f84,
        0x1521b628, 0x29076170, 0xecdd4775, 0x619f1510, 0x13cca830, 0xeb61bd96, 0x0334fe1e, 0xaa0363cf,
        0xb5735c90, 0x4c70a239, 0xd59e9e0b, 0xcbaade14, 0xeecc86bc, 0x60622ca7, 0x9cab5cab, 0xb2f3846e,
        0x648b1eaf, 0x19bdf0ca, 0xa02369b9, 0x655abb50, 0x40685a32, 0x3c2ab4b3, 0x319ee9d5, 0xc021b8f7,
        0x9b540b19, 0x875fa099, 0x95f7997e, 0x623d7da8, 0xf837889a, 0x97e32d77, 0x11ed935f, 0x16681281,
        0x0e358829, 0xc7e61fd6, 0x96dedfa1, 0x7858ba99, 0x57f584a5, 0x1b227263, 0x9b83c3ff, 0x1ac24696,
        0xcdb30aeb, 0x532e3054, 0x8fd948e4, 0x6dbc3128, 0x58ebf2ef, 0x34c6ffea, 0xfe28ed61, 0xee7c3c73,
        0x5d4a14d9, 0xe864b7e3, 0x42105d14, 0x203e13e0, 0x45eee2b6, 0xa3aaabea, 0xdb6c4f15, 0xfacb4fd0,
        0xc742f442, 0xef6abbb5, 0x654f3b1d, 0x41cd2105, 0xd81e799e, 0x86854dc7, 0xe44b476a, 0x3d816250,
        0xcf62a1f2, 0x5b8d2646, 0xfc8883a0, 0xc1c7b6a3, 0x7f1524c3, 0x69cb7492, 0x47848a0b, 0x5692b285,
        0x095bbf00, 0xad19489d, 0x1462b174, 0x23820e00, 0x58428d2a, 0x0c55f5ea, 0x1dadf43e, 0x233f7061,
        0x3372f092, 0x8d937e41, 0xd65fecf1, 0x6c223bdb, 0x7cde3759, 0xcbee7460, 0x4085f2a7, 0xce77326e,
        0xa6078084, 0x19f8509e, 0xe8efd855, 0x61d99735, 0xa969a7aa, 0xc50c06c2, 0x5a04abfc, 0x800bcadc,
        0x9e447a2e, 0xc3453484, 0xfdd56705, 0x0e1e9ec9, 0xdb73dbd3, 0x105588cd, 0x675fda79, 0xe3674340,
        0xc5c43465, 0x713e38d8, 0x3d28f89e, 0xf16dff20, 0x153e21e7, 0x8fb03d4a, 0xe6e39f2b, 0xdb83adf7
    };
    /**
     * The initial values for the third S-box, according to the Blowfish specification.
     */
    private static final int[] INITIAL_S_BOX_3 = new int[]{
        0xe93d5a68, 0x948140f7, 0xf64c261c, 0x94692934, 0x411520f7, 0x7602d4f7, 0xbcf46b2e, 0xd4a20068,
        0xd4082471, 0x3320f46a, 0x43b7d4b7, 0x500061af, 0x1e39f62e, 0x97244546, 0x14214f74, 0xbf8b8840,
        0x4d95fc1d, 0x96b591af, 0x70f4ddd3, 0x66a02f45, 0xbfbc09ec, 0x03bd9785, 0x7fac6dd0, 0x31cb8504,
        0x96eb27b3, 0x55fd3941, 0xda2547e6, 0xabca0a9a, 0x28507825, 0x530429f4, 0x0a2c86da, 0xe9b66dfb,
        0x68dc1462, 0xd7486900, 0x680ec0a4, 0x27a18dee, 0x4f3ffea2, 0xe887ad8c, 0xb58ce006, 0x7af4d6b6,
        0xaace1e7c, 0xd3375fec, 0xce78a399, 0x406b2a42, 0x20fe9e35, 0xd9f385b9, 0xee39d7ab, 0x3b124e8b,
        0x1dc9faf7, 0x4b6d1856, 0x26a36631, 0xeae397b2, 0x3a6efa74, 0xdd5b4332, 0x6841e7f7, 0xca7820fb,
        0xfb0af54e, 0xd8feb397, 0x454056ac, 0xba489527, 0x55533a3a, 0x20838d87, 0xfe6ba9b7, 0xd096954b,
        0x55a867bc, 0xa1159a58, 0xcca92963, 0x99e1db33, 0xa62a4a56, 0x3f3125f9, 0x5ef47e1c, 0x9029317c,
        0xfdf8e802, 0x04272f70, 0x80bb155c, 0x05282ce3, 0x95c11548, 0xe4c66d22, 0x48c1133f, 0xc70f86dc,
        0x07f9c9ee, 0x41041f0f, 0x404779a4, 0x5d886e17, 0x325f51eb, 0xd59bc0d1, 0xf2bcc18f, 0x41113564,
        0x257b7834, 0x602a9c60, 0xdff8e8a3, 0x1f636c1b, 0x0e12b4c2, 0x02e1329e, 0xaf664fd1, 0xcad18115,
        0x6b2395e0, 0x333e92e1, 0x3b240b62, 0xeebeb922, 0x85b2a20e, 0xe6ba0d99, 0xde720c8c, 0x2da2f728,
        0xd0127845, 0x95b794fd, 0x647d0862, 0xe7ccf5f0, 0x5449a36f, 0x877d48fa, 0xc39dfd27, 0xf33e8d1e,
        0x0a476341, 0x992eff74, 0x3a6f6eab, 0xf4f8fd37, 0xa812dc60, 0xa1ebddf8, 0x991be14c, 0xdb6e6b0d,
        0xc67b5510, 0x6d672c37, 0x2765d43b, 0xdcd0e804, 0xf1290dc7, 0xcc00ffa3, 0xb5390f92, 0x690fed0b,
        0x667b9ffb, 0xcedb7d9c, 0xa091cf0b, 0xd9155ea3, 0xbb132f88, 0x515bad24, 0x7b9479bf, 0x763bd6eb,
        0x37392eb3, 0xcc115979, 0x8026e297, 0xf42e312d, 0x6842ada7, 0xc66a2b3b, 0x12754ccc, 0x782ef11c,
        0x6a124237, 0xb79251e7, 0x06a1bbe6, 0x4bfb6350, 0x1a6b1018, 0x11caedfa, 0x3d25bdd8, 0xe2e1c3c9,
        0x44421659, 0x0a121386, 0xd90cec6e, 0xd5abea2a, 0x64af674e, 0xda86a85f, 0xbebfe988, 0x64e4c3fe,
        0x9dbc8057, 0xf0f7c086, 0x60787bf8, 0x6003604d, 0xd1fd8346, 0xf6381fb0, 0x7745ae04, 0xd736fccc,
        0x83426b33, 0xf01eab71, 0xb0804187, 0x3c005e5f, 0x77a057be, 0xbde8ae24, 0x55464299, 0xbf582e61,
        0x4e58f48f, 0xf2ddfda2, 0xf474ef38, 0x8789bdc2, 0x5366f9c3, 0xc8b38e74, 0xb475f255, 0x46fcd9b9,
        0x7aeb2661, 0x8b1ddf84, 0x846a0e79, 0x915f95e2, 0x466e598e, 0x20b45770, 0x8cd55591, 0xc902de4c,
        0xb90bace1, 0xbb8205d0, 0x11a86248, 0x7574a99e, 0xb77f19b6, 0xe0a9dc09, 0x662d09a1, 0xc4324633,
        0xe85a1f02, 0x09f0be8c, 0x4a99a025, 0x1d6efe10, 0x1ab93d1d, 0x0ba5a4df, 0xa186f20f, 0x2868f169,
        0xdcb7da83, 0x573906fe, 0xa1e2ce9b, 0x4fcd7f52, 0x50115e01, 0xa70683fa, 0xa002b5c4, 0x0de6d027,
        0x9af88c27, 0x773f8641, 0xc3604c06, 0x61a806b5, 0xf0177a28, 0xc0f586e0, 0x006058aa, 0x30dc7d62,
        0x11e69ed7, 0x2338ea63, 0x53c2dd94, 0xc2c21634, 0xbbcbee56, 0x90bcb6de, 0xebfc7da1, 0xce591d76,
        0x6f05e409, 0x4b7c0188, 0x39720a3d, 0x7c927c24, 0x86e3725f, 0x724d9db9, 0x1ac15bb4, 0xd39eb8fc,
        0xed545578, 0x08fca5b5, 0xd83d7cd3, 0x4dad0fc4, 0x1e50ef5e, 0xb161e6f8, 0xa28514d9, 0x6c51133c,
        0x6fd5c7e7, 0x56e14ec4, 0x362abfce, 0xddc6c837, 0xd79a3234, 0x92638212, 0x670efa8e, 0x406000e0
    };
    /**
     * The initial values for the fourth S-box, according to the Blowfish specification.
     */
    private static final int[] INITIAL_S_BOX_4 = new int[]{
        0x3a39ce37, 0xd3faf5cf, 0xabc27737, 0x5ac52d1b, 0x5cb0679e, 0x4fa33742, 0xd3822740, 0x99bc9bbe,
        0xd5118e9d, 0xbf0f7315, 0xd62d1c7e, 0xc700c47b, 0xb78c1b6b, 0x21a19045, 0xb26eb1be, 0x6a366eb4,
        0x5748ab2f, 0xbc946e79, 0xc6a376d2, 0x6549c2c8, 0x530ff8ee, 0x468dde7d, 0xd5730a1d, 0x4cd04dc6,
        0x2939bbdb, 0xa9ba4650, 0xac9526e8, 0xbe5ee304, 0xa1fad5f0, 0x6a2d519a, 0x63ef8ce2, 0x9a86ee22,
        0xc089c2b8, 0x43242ef6, 0xa51e03aa, 0x9cf2d0a4, 0x83c061ba, 0x9be96a4d, 0x8fe51550, 0xba645bd6,
        0x2826a2f9, 0xa73a3ae1, 0x4ba99586, 0xef5562e9, 0xc72fefd3, 0xf752f7da, 0x3f046f69, 0x77fa0a59,
        0x80e4a915, 0x87b08601, 0x9b09e6ad, 0x3b3ee593, 0xe990fd5a, 0x9e34d797, 0x2cf0b7d9, 0x022b8b51,
        0x96d5ac3a, 0x017da67d, 0xd1cf3ed6, 0x7c7d2d28, 0x1f9f25cf, 0xadf2b89b, 0x5ad6b472, 0x5a88f54c,
        0xe029ac71, 0xe019a5e6, 0x47b0acfd, 0xed93fa9b, 0xe8d3c48d, 0x283b57cc, 0xf8d56629, 0x79132e28,
        0x785f0191, 0xed756055, 0xf7960e44, 0xe3d35e8c, 0x15056dd4, 0x88f46dba, 0x03a16125, 0x0564f0bd,
        0xc3eb9e15, 0x3c9057a2, 0x97271aec, 0xa93a072a, 0x1b3f6d9b, 0x1e6321f5, 0xf59c66fb, 0x26dcf319,
        0x7533d928, 0xb155fdf5, 0x03563482, 0x8aba3cbb, 0x28517711, 0xc20ad9f8, 0xabcc5167, 0xccad925f,
        0x4de81751, 0x3830dc8e, 0x379d5862, 0x9320f991, 0xea7a90c2, 0xfb3e7bce, 0x5121ce64, 0x774fbe32,
        0xa8b6e37e, 0xc3293d46, 0x48de5369, 0x6413e680, 0xa2ae0810, 0xdd6db224, 0x69852dfd, 0x09072166,
        0xb39a460a, 0x6445c0dd, 0x586cdecf, 0x1c20c8ae, 0x5bbef7dd, 0x1b588d40, 0xccd2017f, 0x6bb4e3bb,
        0xdda26a7e, 0x3a59ff45, 0x3e350a44, 0xbcb4cdd5, 0x72eacea8, 0xfa6484bb, 0x8d6612ae, 0xbf3c6f47,
        0xd29be463, 0x542f5d9e, 0xaec2771b, 0xf64e6370, 0x740e0d8d, 0xe75b1357, 0xf8721671, 0xaf537d5d,
        0x4040cb08, 0x4eb4e2cc, 0x34d2466a, 0x0115af84, 0xe1b00428, 0x95983a1d, 0x06b89fb4, 0xce6ea048,
        0x6f3f3b82, 0x3520ab82, 0x011a1d4b, 0x277227f8, 0x611560b1, 0xe7933fdc, 0xbb3a792b, 0x344525bd,
        0xa08839e1, 0x51ce794b, 0x2f32c9b7, 0xa01fbac9, 0xe01cc87e, 0xbcc7d1f6, 0xcf0111c3, 0xa1e8aac7,
        0x1a908749, 0xd44fbd9a, 0xd0dadecb, 0xd50ada38, 0x0339c32a, 0xc6913667, 0x8df9317c, 0xe0b12b4f,
        0xf79e59b7, 0x43f5bb3a, 0xf2d519ff, 0x27d9459c, 0xbf97222c, 0x15e6fc2a, 0x0f91fc71, 0x9b941525,
        0xfae59361, 0xceb69ceb, 0xc2a86459, 0x12baa8d1, 0xb6c1075e, 0xe3056a0c, 0x10d25065, 0xcb03a442,
        0xe0ec6e0e, 0x1698db3b, 0x4c98a0be, 0x3278e964, 0x9f1f9532, 0xe0d392df, 0xd3a0342b, 0x8971f21e,
        0x1b0a7441, 0x4ba3348c, 0xc5be7120, 0xc37632d8, 0xdf359f8d, 0x9b992f2e, 0xe60b6f47, 0x0fe3f11d,
        0xe54cda54, 0x1edad891, 0xce6279cf, 0xcd3e7e6f, 0x1618b166, 0xfd2c1d05, 0x848fd2c5, 0xf6fb2299,
        0xf523f357, 0xa6327623, 0x93a83531, 0x56cccd02, 0xacf08162, 0x5a75ebb5, 0x6e163697, 0x88d273cc,
        0xde966292, 0x81b949d0, 0x4c50901b, 0x71c65614, 0xe6c6c7bd, 0x327a140a, 0x45e1d006, 0xc3f27b9a,
        0xc9aa53fd, 0x62a80f00, 0xbb25bfe2, 0x35bdd2f6, 0x71126905, 0xb2040222, 0xb6cbcf7c, 0xcd769c2b,
        0x53113ec0, 0x1640e3d3, 0x38abbd60, 0x2547adf0, 0xba38209c, 0xf746ce76, 0x77afa1c5, 0x20756060,
        0x85cbfe4e, 0x8ae88dd8, 0x7aaaf9b0, 0x4cf9aa7e, 0x1948c25c, 0x02fb8a8c, 0x01c36ae4, 0xd6ebe1f9,
        0x90d4f869, 0xa65cdea0, 0x3f09252d, 0xc208e69f, 0xb74e6132, 0xce77e25b, 0x578fdfe3, 0x3ac372e6
    };

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * Element 1 in the P-array of the Blowfish algorithm.
     */
    private int p1;
    /**
     * Element 2 in the P-array of the Blowfish algorithm.
     */
    private int p2;
    /**
     * Element 3 in the P-array of the Blowfish algorithm.
     */
    private int p3;
    /**
     * Element 4 in the P-array of the Blowfish algorithm.
     */
    private int p4;
    /**
     * Element 5 in the P-array of the Blowfish algorithm.
     */
    private int p5;
    /**
     * Element 6 in the P-array of the Blowfish algorithm.
     */
    private int p6;
    /**
     * Element 7 in the P-array of the Blowfish algorithm.
     */
    private int p7;
    /**
     * Element 8 in the P-array of the Blowfish algorithm.
     */
    private int p8;
    /**
     * Element 9 in the P-array of the Blowfish algorithm.
     */
    private int p9;
    /**
     * Element 10 in the P-array of the Blowfish algorithm.
     */
    private int p10;
    /**
     * Element 11 in the P-array of the Blowfish algorithm.
     */
    private int p11;
    /**
     * Element 12 in the P-array of the Blowfish algorithm.
     */
    private int p12;
    /**
     * Element 13 in the P-array of the Blowfish algorithm.
     */
    private int p13;
    /**
     * Element 14 in the P-array of the Blowfish algorithm.
     */
    private int p14;
    /**
     * Element 15 in the P-array of the Blowfish algorithm.
     */
    private int p15;
    /**
     * Element 16 in the P-array of the Blowfish algorithm.
     */
    private int p16;
    /**
     * Element 17 in the P-array of the Blowfish algorithm.
     */
    private int p17;
    /**
     * Element 18 in the P-array of the Blowfish algorithm.
     */
    private int p18;
    /**
     * The first S-box for the Blowfish algorithm.
     */
    private int[] s1;
    /**
     * The second S-box for the Blowfish algorithm.
     */
    private int[] s2;
    /**
     * The third S-box for the Blowfish algorithm.
     */
    private int[] s3;
    /**
     * The fourth S-box for the Blowfish algorithm.
     */
    private int[] s4;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes the entire array reprsents the key.
     *
     * @param key The array in which resides the key to be passed to the algorithm.
     */
    public Blowfish(byte[] key)
    {
        super();
        initialize();
        setKey(key);
    }

    /**
     * Skeleton constructor.  Assumes the key begins at the beginning of the provided array.
     *
     * @param key  The array in which resides the key to be passed to the algorithm.
     * @param bits The number of bits relevant in the provided key.
     */
    public Blowfish(byte[] key, int bits)
    {
        super();
        initialize();
        setKey(key, bits);
    }

    /**
     * General constructor.
     *
     * @param key    The array in which resides the key to be passed to the algorithm.
     * @param offset The index of the starting <b>bit</b> of the key.  Note this distinction.
     * @param bits   The number of bits relevant in the provided key.
     */
    public Blowfish(byte[] key, int offset, int bits)
    {
        super();
        initialize();
        setKey(key, offset, bits);
    }

    /**
     * Called by all constructors to perform any common initialization for this cryptographic implementation.
     */
    private void initialize()
    {
        s1 = new int[256];
        s2 = new int[256];
        s3 = new int[256];
        s4 = new int[256];
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Sets the key for this algorithm.  Assumes that the entire provided byte array is the key.  The key may not be
     * larger than 56 bytes.
     *
     * @param key The key, contained within a <code>byte[]</code>.
     * @throws IllegalArgumentException If the specified key has a length of <code>0</code> or less bits or has a length
     *                                  of more than <code>448</code> bits (<code>56</code> bytes).
     */
    public void setKey(byte[] key)
    {
        setKey(key, 0, key.length * 8);
    }

    /**
     * Sets the key for this algorithm.  Assumes that the key begins at bit zero and extends for the specified number of
     * bits.  The remaining bits are ignored.  The key may not be larger than 56 bytes.
     *
     * @param key  The array containing the key.
     * @param bits The length of the key, in bits.
     * @throws IllegalArgumentException If the specified key has a length of <code>0</code> or less bits or has a length
     *                                  of more than <code>448</code> bits (<code>56</code> bytes).
     */
    public void setKey(byte[] key, int bits)
    {
        setKey(key, 0, bits);
    }

    /**
     * Sets the key for this algorithm.  The key starts at the bit offset specified and extends for the specified number
     * of bits.  All other information in the array storing the key is ignored.  The key may not be larger than 56
     * bytes.
     *
     * @param key    The array containing the key.
     * @param offset The offset of the first bit of the key.
     * @param bits   The length of the key, in bits.
     * @throws IllegalArgumentException If the specified key has a length of <code>0</code> or less bits or has a length
     *                                  of more than <code>448</code> bits (<code>56</code> bytes).
     */
    public void setKey(byte[] key, int offset, int bits)
    {
        if ((bits < 1) || (bits > 448)) throw new IllegalArgumentException("Invalid key length: " + bits + " bits");
        // Initialize P-array and S-boxes
        p1 = 0x243f6a88;
        p2 = 0x85a308d3;
        p3 = 0x13198a2e;
        p4 = 0x03707344;
        p5 = 0xa4093822;
        p6 = 0x299f31d0;
        p7 = 0x082efa98;
        p8 = 0xec4e6c89;
        p9 = 0x452821e6;
        p10 = 0x38d01377;
        p11 = 0xbe5466cf;
        p12 = 0x34e90c6c;
        p13 = 0xc0ac29b7;
        p14 = 0xc97c50dd;
        p15 = 0x3f84d5b5;
        p16 = 0xb5470917;
        p17 = 0x9216d5d9;
        p18 = 0x8979fb1b;
        System.arraycopy(INITIAL_S_BOX_1, 0, s1, 0, 256);
        System.arraycopy(INITIAL_S_BOX_2, 0, s2, 0, 256);
        System.arraycopy(INITIAL_S_BOX_3, 0, s3, 0, 256);
        System.arraycopy(INITIAL_S_BOX_4, 0, s4, 0, 256);

        // Perform key XOR
        byte[] xordata = Utilities.createBitPatternArray(key, offset, bits, 72);
        p1 ^= DataConversion.byteArrayToInt(xordata, 0);
        p2 ^= DataConversion.byteArrayToInt(xordata, 4);
        p3 ^= DataConversion.byteArrayToInt(xordata, 8);
        p4 ^= DataConversion.byteArrayToInt(xordata, 12);
        p5 ^= DataConversion.byteArrayToInt(xordata, 16);
        p6 ^= DataConversion.byteArrayToInt(xordata, 20);
        p7 ^= DataConversion.byteArrayToInt(xordata, 24);
        p8 ^= DataConversion.byteArrayToInt(xordata, 28);
        p9 ^= DataConversion.byteArrayToInt(xordata, 32);
        p10 ^= DataConversion.byteArrayToInt(xordata, 36);
        p11 ^= DataConversion.byteArrayToInt(xordata, 40);
        p12 ^= DataConversion.byteArrayToInt(xordata, 44);
        p13 ^= DataConversion.byteArrayToInt(xordata, 48);
        p14 ^= DataConversion.byteArrayToInt(xordata, 52);
        p15 ^= DataConversion.byteArrayToInt(xordata, 58);
        p16 ^= DataConversion.byteArrayToInt(xordata, 60);
        p17 ^= DataConversion.byteArrayToInt(xordata, 64);
        p18 ^= DataConversion.byteArrayToInt(xordata, 68);

        // Perform key generation
        byte[] keyGeneration = new byte[8];
        encrypt(keyGeneration);
        p1 = DataConversion.byteArrayToInt(keyGeneration, 0);
        p2 = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        p3 = DataConversion.byteArrayToInt(keyGeneration, 0);
        p4 = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        p5 = DataConversion.byteArrayToInt(keyGeneration, 0);
        p6 = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        p7 = DataConversion.byteArrayToInt(keyGeneration, 0);
        p8 = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        p9 = DataConversion.byteArrayToInt(keyGeneration, 0);
        p10 = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        p11 = DataConversion.byteArrayToInt(keyGeneration, 0);
        p12 = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        p13 = DataConversion.byteArrayToInt(keyGeneration, 0);
        p14 = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        p15 = DataConversion.byteArrayToInt(keyGeneration, 0);
        p16 = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        p17 = DataConversion.byteArrayToInt(keyGeneration, 0);
        p18 = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[0] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[1] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[2] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[3] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[4] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[5] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[6] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[7] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[8] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[9] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[10] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[11] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[12] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[13] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[14] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[15] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[16] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[17] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[18] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[19] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[20] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[21] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[22] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[23] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[24] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[25] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[26] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[27] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[28] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[29] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[30] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[31] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[32] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[33] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[34] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[35] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[36] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[37] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[38] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[39] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[40] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[41] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[42] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[43] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[44] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[45] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[46] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[47] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[48] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[49] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[50] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[51] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[52] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[53] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[54] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[55] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[56] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[57] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[58] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[59] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[60] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[61] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[62] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[63] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[64] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[65] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[66] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[67] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[68] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[69] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[70] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[71] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[72] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[73] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[74] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[75] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[76] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[77] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[78] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[79] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[80] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[81] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[82] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[83] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[84] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[85] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[86] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[87] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[88] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[89] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[90] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[91] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[92] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[93] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[94] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[95] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[96] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[97] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[98] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[99] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[100] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[101] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[102] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[103] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[104] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[105] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[106] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[107] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[108] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[109] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[110] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[111] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[112] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[113] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[114] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[115] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[116] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[117] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[118] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[119] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[120] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[121] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[122] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[123] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[124] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[125] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[126] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[127] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[128] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[129] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[130] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[131] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[132] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[133] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[134] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[135] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[136] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[137] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[138] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[139] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[140] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[141] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[142] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[143] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[144] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[145] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[146] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[147] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[148] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[149] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[150] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[151] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[152] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[153] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[154] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[155] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[156] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[157] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[158] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[159] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[160] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[161] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[162] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[163] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[164] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[165] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[166] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[167] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[168] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[169] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[170] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[171] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[172] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[173] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[174] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[175] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[176] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[177] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[178] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[179] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[180] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[181] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[182] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[183] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[184] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[185] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[186] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[187] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[188] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[189] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[190] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[191] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[192] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[193] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[194] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[195] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[196] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[197] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[198] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[199] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[200] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[201] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[202] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[203] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[204] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[205] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[206] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[207] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[208] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[209] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[210] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[211] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[212] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[213] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[214] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[215] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[216] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[217] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[218] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[219] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[220] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[221] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[222] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[223] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[224] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[225] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[226] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[227] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[228] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[229] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[230] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[231] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[232] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[233] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[234] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[235] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[236] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[237] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[238] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[239] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[240] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[241] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[242] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[243] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[244] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[245] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[246] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[247] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[248] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[249] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[250] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[251] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[252] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[253] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s1[254] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s1[255] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[0] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[1] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[2] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[3] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[4] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[5] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[6] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[7] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[8] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[9] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[10] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[11] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[12] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[13] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[14] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[15] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[16] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[17] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[18] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[19] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[20] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[21] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[22] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[23] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[24] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[25] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[26] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[27] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[28] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[29] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[30] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[31] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[32] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[33] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[34] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[35] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[36] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[37] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[38] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[39] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[40] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[41] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[42] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[43] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[44] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[45] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[46] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[47] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[48] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[49] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[50] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[51] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[52] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[53] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[54] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[55] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[56] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[57] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[58] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[59] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[60] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[61] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[62] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[63] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[64] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[65] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[66] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[67] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[68] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[69] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[70] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[71] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[72] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[73] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[74] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[75] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[76] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[77] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[78] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[79] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[80] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[81] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[82] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[83] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[84] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[85] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[86] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[87] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[88] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[89] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[90] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[91] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[92] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[93] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[94] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[95] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[96] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[97] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[98] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[99] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[100] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[101] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[102] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[103] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[104] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[105] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[106] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[107] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[108] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[109] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[110] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[111] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[112] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[113] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[114] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[115] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[116] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[117] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[118] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[119] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[120] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[121] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[122] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[123] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[124] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[125] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[126] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[127] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[128] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[129] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[130] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[131] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[132] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[133] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[134] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[135] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[136] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[137] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[138] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[139] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[140] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[141] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[142] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[143] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[144] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[145] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[146] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[147] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[148] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[149] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[150] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[151] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[152] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[153] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[154] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[155] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[156] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[157] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[158] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[159] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[160] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[161] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[162] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[163] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[164] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[165] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[166] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[167] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[168] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[169] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[170] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[171] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[172] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[173] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[174] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[175] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[176] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[177] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[178] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[179] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[180] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[181] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[182] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[183] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[184] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[185] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[186] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[187] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[188] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[189] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[190] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[191] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[192] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[193] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[194] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[195] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[196] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[197] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[198] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[199] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[200] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[201] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[202] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[203] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[204] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[205] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[206] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[207] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[208] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[209] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[210] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[211] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[212] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[213] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[214] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[215] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[216] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[217] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[218] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[219] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[220] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[221] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[222] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[223] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[224] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[225] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[226] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[227] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[228] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[229] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[230] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[231] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[232] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[233] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[234] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[235] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[236] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[237] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[238] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[239] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[240] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[241] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[242] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[243] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[244] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[245] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[246] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[247] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[248] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[249] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[250] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[251] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[252] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[253] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s2[254] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s2[255] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[0] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[1] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[2] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[3] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[4] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[5] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[6] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[7] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[8] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[9] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[10] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[11] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[12] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[13] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[14] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[15] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[16] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[17] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[18] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[19] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[20] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[21] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[22] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[23] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[24] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[25] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[26] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[27] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[28] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[29] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[30] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[31] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[32] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[33] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[34] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[35] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[36] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[37] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[38] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[39] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[40] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[41] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[42] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[43] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[44] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[45] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[46] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[47] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[48] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[49] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[50] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[51] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[52] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[53] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[54] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[55] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[56] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[57] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[58] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[59] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[60] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[61] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[62] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[63] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[64] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[65] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[66] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[67] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[68] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[69] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[70] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[71] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[72] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[73] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[74] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[75] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[76] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[77] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[78] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[79] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[80] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[81] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[82] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[83] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[84] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[85] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[86] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[87] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[88] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[89] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[90] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[91] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[92] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[93] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[94] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[95] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[96] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[97] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[98] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[99] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[100] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[101] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[102] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[103] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[104] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[105] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[106] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[107] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[108] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[109] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[110] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[111] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[112] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[113] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[114] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[115] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[116] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[117] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[118] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[119] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[120] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[121] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[122] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[123] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[124] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[125] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[126] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[127] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[128] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[129] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[130] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[131] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[132] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[133] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[134] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[135] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[136] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[137] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[138] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[139] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[140] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[141] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[142] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[143] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[144] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[145] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[146] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[147] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[148] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[149] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[150] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[151] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[152] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[153] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[154] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[155] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[156] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[157] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[158] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[159] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[160] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[161] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[162] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[163] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[164] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[165] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[166] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[167] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[168] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[169] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[170] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[171] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[172] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[173] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[174] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[175] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[176] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[177] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[178] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[179] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[180] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[181] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[182] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[183] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[184] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[185] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[186] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[187] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[188] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[189] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[190] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[191] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[192] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[193] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[194] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[195] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[196] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[197] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[198] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[199] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[200] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[201] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[202] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[203] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[204] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[205] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[206] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[207] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[208] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[209] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[210] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[211] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[212] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[213] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[214] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[215] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[216] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[217] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[218] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[219] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[220] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[221] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[222] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[223] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[224] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[225] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[226] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[227] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[228] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[229] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[230] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[231] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[232] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[233] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[234] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[235] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[236] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[237] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[238] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[239] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[240] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[241] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[242] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[243] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[244] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[245] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[246] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[247] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[248] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[249] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[250] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[251] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[252] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[253] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s3[254] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s3[255] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[0] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[1] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[2] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[3] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[4] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[5] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[6] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[7] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[8] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[9] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[10] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[11] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[12] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[13] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[14] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[15] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[16] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[17] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[18] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[19] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[20] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[21] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[22] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[23] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[24] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[25] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[26] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[27] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[28] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[29] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[30] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[31] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[32] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[33] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[34] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[35] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[36] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[37] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[38] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[39] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[40] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[41] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[42] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[43] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[44] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[45] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[46] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[47] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[48] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[49] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[50] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[51] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[52] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[53] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[54] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[55] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[56] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[57] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[58] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[59] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[60] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[61] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[62] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[63] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[64] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[65] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[66] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[67] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[68] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[69] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[70] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[71] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[72] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[73] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[74] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[75] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[76] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[77] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[78] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[79] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[80] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[81] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[82] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[83] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[84] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[85] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[86] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[87] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[88] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[89] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[90] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[91] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[92] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[93] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[94] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[95] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[96] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[97] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[98] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[99] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[100] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[101] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[102] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[103] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[104] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[105] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[106] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[107] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[108] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[109] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[110] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[111] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[112] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[113] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[114] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[115] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[116] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[117] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[118] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[119] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[120] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[121] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[122] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[123] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[124] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[125] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[126] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[127] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[128] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[129] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[130] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[131] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[132] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[133] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[134] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[135] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[136] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[137] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[138] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[139] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[140] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[141] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[142] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[143] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[144] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[145] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[146] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[147] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[148] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[149] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[150] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[151] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[152] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[153] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[154] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[155] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[156] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[157] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[158] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[159] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[160] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[161] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[162] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[163] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[164] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[165] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[166] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[167] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[168] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[169] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[170] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[171] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[172] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[173] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[174] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[175] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[176] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[177] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[178] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[179] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[180] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[181] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[182] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[183] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[184] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[185] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[186] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[187] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[188] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[189] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[190] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[191] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[192] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[193] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[194] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[195] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[196] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[197] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[198] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[199] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[200] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[201] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[202] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[203] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[204] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[205] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[206] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[207] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[208] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[209] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[210] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[211] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[212] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[213] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[214] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[215] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[216] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[217] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[218] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[219] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[220] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[221] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[222] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[223] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[224] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[225] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[226] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[227] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[228] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[229] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[230] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[231] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[232] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[233] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[234] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[235] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[236] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[237] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[238] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[239] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[240] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[241] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[242] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[243] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[244] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[245] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[246] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[247] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[248] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[249] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[250] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[251] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[252] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[253] = DataConversion.byteArrayToInt(keyGeneration, 4);
        encrypt(keyGeneration);
        s4[254] = DataConversion.byteArrayToInt(keyGeneration, 0);
        s4[255] = DataConversion.byteArrayToInt(keyGeneration, 4);
    }

    /**
     * Encrypts the provided data using the present key.  The encrypted array is stored in the array parameter.
     *
     * @param data The array containing the data to encrypt.
     * @return The encrypted form of the data in the same array.
     * @throws IllegalArgumentException If the array is not of a length divisible by <code>8</code>.
     */
    public byte[] encrypt(byte[] data)
            throws IllegalArgumentException
    {
        if (data.length % 8 != 0) throw new IllegalArgumentException(
                "Array length must be divisible by eight.  This array was " + data.length + " bytes.");
        int maxloop = data.length / 8;
        for (int i = 0; i < maxloop; i++)
        {
            encrypt(data, i * 8);
        }
        return data;
    }

    /**
     * Encrypts part of the provided data using the present key.  The encrypted data is stored in the array parameter.
     * The rest of the array is not affected.
     *
     * @param data   The array containing the data to encrypt.
     * @param offset The offset of the data to encrypt.
     * @return The encrypted form of that data in the same array.  The other data in the array is not affected.
     * @throws IndexOutOfBoundsException If there are not at least eight bytes following the specified offset.
     */
    public byte[] encrypt(byte[] data, int offset)
    {
        return cipher(
                data,
                offset,
                p1,
                p2,
                p3,
                p4,
                p5,
                p6,
                p7,
                p8,
                p9,
                p10,
                p11,
                p12,
                p13,
                p14,
                p15,
                p16,
                p17,
                p18);
    }

    /**
     * Decrypts the provided data using the present key.  The decrypted array is stored in the array parameter.
     *
     * @param data The array containing the encrypted data.
     * @return The decrypted form of the data in the same array.
     * @throws IllegalArgumentException If the array is not of a length divisible by <code>8</code>.
     */
    public byte[] decrypt(byte[] data)
            throws IllegalArgumentException
    {
        if (data.length % 8 != 0) throw new IllegalArgumentException(
                "Array length must be divisible by eight.  This array was " + data.length + " bytes.");
        int maxloop = data.length / 8;
        for (int i = 0; i < maxloop; i++)
        {
            decrypt(data, i * 8);
        }
        return data;
    }

    /**
     * Decrypts part of the provided data using the present key.  The decrypted data is stored in the array parameter.
     * The rest of the array is not affected.
     *
     * @param data   The array containing the data to decrypt.
     * @param offset The offset of the data to encrypt.
     * @return The decrypted form of that data in the same array.  The other data in the array is not affected.
     * @throws IndexOutOfBoundsException If there are not at least eight bytes following the specified offset.
     */
    public byte[] decrypt(byte[] data, int offset)
    {
        return cipher(
                data,
                offset,
                p18,
                p17,
                p16,
                p15,
                p14,
                p13,
                p12,
                p11,
                p10,
                p9,
                p8,
                p7,
                p6,
                p5,
                p4,
                p3,
                p2,
                p1);
    }

// NON-STATIC METHODS : INTERNAL /////////////////////////////////////////////////

    /**
     * Performs a blowfish cipher operation using the specified p-values.  This method is used by both the encryption
     * and decryption methods.
     *
     * @param data   The array containing the data to cipher.
     * @param offset The offset of the first byte to cipher.
     * @param p1     The first p-value to use.
     * @param p2     The second p-value to use.
     * @param p3     The third p-value to use.
     * @param p4     The fourth p-value to use.
     * @param p5     The fifth p-value to use.
     * @param p6     The sixth p-value to use.
     * @param p7     The seventh p-value to use.
     * @param p8     The eighth p-value to use.
     * @param p9     The ninth p-value to use.
     * @param p10    The tenth p-value to use.
     * @param p11    The eleventh p-value to use.
     * @param p12    The twelfth p-value to use.
     * @param p13    The thirteenth p-value to use.
     * @param p14    The fourteenth p-value to use.
     * @param p15    The fifteenth p-value to use.
     * @param p16    The sixteenth p-value to use.
     * @param p17    The seventeenth p-value to use.
     * @param p18    The eightteenth p-value to use.
     * @return The same array with eight bytes, starting at the specified offset, encrypted with the current key and
     *         provided p-values.
     * @throws IllegalArgumentException If the array does not contain at least eight bytes following the specified
     *                                  offset.
     */
    private byte[] cipher(byte[] data, int offset, int p1, int p2, int p3, int p4, int p5, int p6, int p7, int p8,
                          int p9, int p10, int p11, int p12, int p13, int p14, int p15, int p16, int p17, int p18)
            throws IllegalArgumentException
    {
        int xL = DataConversion.byteArrayToInt(data, offset);
        int xR = DataConversion.byteArrayToInt(data, offset + 4);
        int temp;
        // Begin Feistel cipher loop (unrolled)
        xL ^= p1;
        xR ^= ((s1[(xL >>> 24) & 0xFF] + s2[(xL >>> 16) & 0xFF]) ^ s3[(xL >>> 8) & 0xFF]) + s4[(xL) & 0xFF];
        temp = xL;
        xL = xR;
        xR = temp;
        xL ^= p2;
        xR ^= ((s1[(xL >>> 24) & 0xFF] + s2[(xL >>> 16) & 0xFF]) ^ s3[(xL >>> 8) & 0xFF]) + s4[(xL) & 0xFF];
        temp = xL;
        xL = xR;
        xR = temp;
        xL ^= p3;
        xR ^= ((s1[(xL >>> 24) & 0xFF] + s2[(xL >>> 16) & 0xFF]) ^ s3[(xL >>> 8) & 0xFF]) + s4[(xL) & 0xFF];
        temp = xL;
        xL = xR;
        xR = temp;
        xL ^= p4;
        xR ^= ((s1[(xL >>> 24) & 0xFF] + s2[(xL >>> 16) & 0xFF]) ^ s3[(xL >>> 8) & 0xFF]) + s4[(xL) & 0xFF];
        temp = xL;
        xL = xR;
        xR = temp;
        xL ^= p5;
        xR ^= ((s1[(xL >>> 24) & 0xFF] + s2[(xL >>> 16) & 0xFF]) ^ s3[(xL >>> 8) & 0xFF]) + s4[(xL) & 0xFF];
        temp = xL;
        xL = xR;
        xR = temp;
        xL ^= p6;
        xR ^= ((s1[(xL >>> 24) & 0xFF] + s2[(xL >>> 16) & 0xFF]) ^ s3[(xL >>> 8) & 0xFF]) + s4[(xL) & 0xFF];
        temp = xL;
        xL = xR;
        xR = temp;
        xL ^= p7;
        xR ^= ((s1[(xL >>> 24) & 0xFF] + s2[(xL >>> 16) & 0xFF]) ^ s3[(xL >>> 8) & 0xFF]) + s4[(xL) & 0xFF];
        temp = xL;
        xL = xR;
        xR = temp;
        xL ^= p8;
        xR ^= ((s1[(xL >>> 24) & 0xFF] + s2[(xL >>> 16) & 0xFF]) ^ s3[(xL >>> 8) & 0xFF]) + s4[(xL) & 0xFF];
        temp = xL;
        xL = xR;
        xR = temp;
        xL ^= p9;
        xR ^= ((s1[(xL >>> 24) & 0xFF] + s2[(xL >>> 16) & 0xFF]) ^ s3[(xL >>> 8) & 0xFF]) + s4[(xL) & 0xFF];
        temp = xL;
        xL = xR;
        xR = temp;
        xL ^= p10;
        xR ^= ((s1[(xL >>> 24) & 0xFF] + s2[(xL >>> 16) & 0xFF]) ^ s3[(xL >>> 8) & 0xFF]) + s4[(xL) & 0xFF];
        temp = xL;
        xL = xR;
        xR = temp;
        xL ^= p11;
        xR ^= ((s1[(xL >>> 24) & 0xFF] + s2[(xL >>> 16) & 0xFF]) ^ s3[(xL >>> 8) & 0xFF]) + s4[(xL) & 0xFF];
        temp = xL;
        xL = xR;
        xR = temp;
        xL ^= p12;
        xR ^= ((s1[(xL >>> 24) & 0xFF] + s2[(xL >>> 16) & 0xFF]) ^ s3[(xL >>> 8) & 0xFF]) + s4[(xL) & 0xFF];
        temp = xL;
        xL = xR;
        xR = temp;
        xL ^= p13;
        xR ^= ((s1[(xL >>> 24) & 0xFF] + s2[(xL >>> 16) & 0xFF]) ^ s3[(xL >>> 8) & 0xFF]) + s4[(xL) & 0xFF];
        temp = xL;
        xL = xR;
        xR = temp;
        xL ^= p14;
        xR ^= ((s1[(xL >>> 24) & 0xFF] + s2[(xL >>> 16) & 0xFF]) ^ s3[(xL >>> 8) & 0xFF]) + s4[(xL) & 0xFF];
        temp = xL;
        xL = xR;
        xR = temp;
        xL ^= p15;
        xR ^= ((s1[(xL >>> 24) & 0xFF] + s2[(xL >>> 16) & 0xFF]) ^ s3[(xL >>> 8) & 0xFF]) + s4[(xL) & 0xFF];
        temp = xL;
        xL = xR;
        xR = temp;
        xL ^= p16;
        xR ^= ((s1[(xL >>> 24) & 0xFF] + s2[(xL >>> 16) & 0xFF]) ^ s3[(xL >>> 8) & 0xFF]) + s4[(xL) & 0xFF];
        // End Feistel cipher loop.
        // Note that the last swap did not occur and therefore does not need to be reversed.
        xR ^= p17;
        xL ^= p18;
        DataConversion.storeIntInByteArray(xL, data, offset);
        DataConversion.storeIntInByteArray(xR, data, offset + 4);
        return data;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}