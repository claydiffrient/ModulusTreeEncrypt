package edu.byui.cs480.modulustree;

import java.lang.reflect.Array;

/**
 * Created by clay on 5/16/14.
 */
public class CryptographyUtilities
{
    public static byte[] EnsurePositive(byte[] bytes)
    {
        byte[] temp = new byte[bytes.length];
        System.arraycopy(bytes, 0, temp, 0, bytes.length);
        bytes = new byte[temp.length + 1];
        System.arraycopy(temp, 0, bytes, 0, temp.length);
        return bytes;
    }

    public static void PadToMultipleOf(byte[] src, int pad)
    {
        int len = (src.length + pad - 1) / pad * pad;
        byte[] temp = new byte[len];
        System.arraycopy(src, 0, temp, 0, temp.length);
    }
}
