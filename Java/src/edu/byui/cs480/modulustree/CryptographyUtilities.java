package edu.byui.cs480.modulustree;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * Created by clay on 5/16/14.
 */
public class CryptographyUtilities
{
//    public static byte[] EnsurePositive(byte[] bytes)
//    {
//        //bytes = Arrays.copyOfRange(bytes, 1, bytes.length);
//
//        bytes[0] = (byte) (bytes[0] & 0xff);
//
//        /*byte[] temp = new byte[bytes.length];
//        System.arraycopy(bytes, 0, temp, 0, bytes.length);
//        bytes = new byte[temp.length + 1];
//        System.arraycopy(temp, 0, bytes, 0, temp.length); */
//
//        return bytes;
//    }

    public static BigInteger bytesToPosBigInteger(byte[] pBytes)
    {
        BigInteger output = BigInteger.ZERO;
        for (int i = 0; i < pBytes.length; i++)
        {
            output = (output.shiftLeft(8)).add(new BigInteger(Integer.toString(pBytes[i] & 0xff)));
        }
        return output;
    }

    public static byte[] PadToMultipleOf(byte[] src, int pad)
    {
        int len = (src.length + pad - 1) / pad * pad;
        byte[] temp = new byte[len];
        System.arraycopy(src, 0, temp, len - src.length, src.length);
        return temp;
    }
}
