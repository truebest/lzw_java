package ru.truebest;

import java.io.*;

public class Main {

    static DataInputStream in = null;
    static DataOutputStream out = null;

    public static void main(String[] args) {

        LzwDecoder lzwDecoder = new LzwDecoder();
        lzwDecoder.registerCallBacks(Main::readData, Main::writeData);
        lzwDecoder.LzwLoadDictionary(args[1]);
        lzwDecoder.ResetContext(LzwDecoder.DEFAULT_CODE_SIZE);

        try {
            in = new DataInputStream(new BufferedInputStream(
                    new FileInputStream(args[0])));
            out = new DataOutputStream(new BufferedOutputStream(
                    new FileOutputStream(args[2])));

            byte[] tmp_array;

            do {
                tmp_array = in.readNBytes(256);
                lzwDecoder.Decode(tmp_array);
            }while (tmp_array.length > 0);


        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Hello World!");

        // write your code here
    }

    private static void writeData(byte[] buf, int code) {
        try {
            out.write(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readData(byte[] buf, int code) {

    }
}
