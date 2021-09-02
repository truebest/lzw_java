package ru.truebest;

public class LzwDecoder extends LzwBase{

    public static int DEFAULT_CODE_SIZE = 17;
    interface Callback{
        void callingBack(byte[] buf, int code);
    }

    Callback f_write;
    Callback f_read;
    private byte[] inbuff;
    private byte c;

    public void registerCallBacks(Callback f_read, Callback f_write){
        this.f_read  = f_read;
        this.f_write = f_write;
    }

    public LzwDecoder() {
        super();
    }

    public void ResetContext(int code_size) {
        this.code = CODE_NULL;
        this.code_size = code_size;
        this.bit_n = 0;
        this.en_dic = false;
    }

    private int LzwDecoderReadBits(int n_bits) {
        while (bit_n < n_bits) {
            if (lzwn == lzwm)
                return -1;
            short tc = (short) (inbuff[lzwn++] & 0xFF);
            bit_buf = (bit_buf << 8) | tc;
            bit_n += 8;
        }
        bit_n -= n_bits;
        return (int) ((bit_buf >> bit_n) & ((1 << n_bits)-1));
    }

    private int LzwDecoderGetStr(int code) {
        int i = LzwBase.TMP_BUF_SIZE;
        while (code != LzwBase.CODE_NULL && i != 0)
        {
            t_buff[--i] = dictionary[code].ch;
            code = dictionary[code].prev;
        }
        return (LzwBase.TMP_BUF_SIZE - i);
    }

    private int LzwDecoderAddStr(int code, byte c) {
        if (code == LzwBase.CODE_NULL)
            return c;

        if (++max == CODE_NULL)
            return CODE_NULL;

        dictionary[max].prev = code;
        dictionary[max].ch = c;
        return max;
    }

    private byte LzwDecoderWriteStr(int code) {
        int len = LzwDecoderGetStr(code);
        byte[] result = new byte[len];
        System.arraycopy(t_buff, t_buff.length - len, result, 0, len);
        f_write.callingBack(result,  len);
        return t_buff[t_buff.length - len];
    }

    public int Decode(byte[] data) {
        inbuff = data;
        lzwn = 0;
        lzwm = data.length;
        for (;;) {
            int ncode;
            ncode = LzwDecoderReadBits(code_size);
            if (ncode < 0) {
                break;
            }
            this.c = LzwDecoderWriteStr(ncode);
            code = ncode;
        }
        return lzwn;
    }

}
