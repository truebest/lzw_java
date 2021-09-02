package ru.truebest;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


abstract class LzwBase {
    public static int CODE_NULL = (1 << 17);
    public static int TMP_BUF_SIZE = 256;
    public static int SIZEOF_NODE_BYTES = 9;
    public int code;
    public int max;
    public int code_size;
    public long bit_buf;
    public int bit_n;
    public int lzwn;
    public int lzwm;
    public LzwNode dictionary[];
    public byte t_buff[];
    public boolean  en_dic;
    public int dh_size;

    public LzwBase() {
        this.code = CODE_NULL;
        this.max      = 255;
        this.code_size = 8;
        this.bit_n = 0;
        this.en_dic = true;
        this.t_buff = new byte[TMP_BUF_SIZE];
    }

    public void LzwCreateDictionary(int dict_size) {
        this.dh_size = dict_size;
        this.dictionary = new LzwNode[dict_size];
        for (int i = 0; i < dict_size; i++ ) {
            this.dictionary[i] = new LzwNode(CODE_NULL, CODE_NULL, (byte)i);
        }
    }

    public void LzwLoadDictionary(final String path) {
        try {
            byte[] data = Files.readAllBytes(Paths.get(path));
            LzwLoadDictionary(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void LzwLoadDictionary(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        if (buffer.limit() > 4) {
            int num_nodes = buffer.getInt();
            if (num_nodes > 0) {
                this.dh_size = num_nodes;
                this.dictionary = new LzwNode[num_nodes];
                for (int i = 0; i < num_nodes; i++) {
                    this.dictionary[i] = new LzwNode(buffer.getInt(), buffer.getInt(), buffer.get());
                }
            }
        }
    }

    public void LzwReset() {
        this.code = CODE_NULL;
        this.max = 255;
        this.code_size = 8;
    }
}
