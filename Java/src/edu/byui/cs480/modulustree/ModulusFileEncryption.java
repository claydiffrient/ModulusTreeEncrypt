package edu.byui.cs480.modulustree;

import com.sun.org.apache.xml.internal.security.encryption.EncryptedType;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by clay on 5/17/14.
 */
public class ModulusFileEncryption
{
    private static final int CHUNK_SIZE = 16;

    public static Byte[] encryptBytes(ModulusTreeKey pKey, Byte[] pInputBytes)
    {
        int chunkCount = (pInputBytes.length + 1) / CHUNK_SIZE;
        int extraBytes = (pInputBytes.length + 1) % CHUNK_SIZE;

        Byte[] pInputBytesPlusTrim = new Byte[pInputBytes.length + 1];
        pInputBytesPlusTrim[0] = (byte)(16 - extraBytes);
        System.arraycopy(pInputBytes, 0, pInputBytesPlusTrim, 1, pInputBytes.length);

        List<BigInteger> values = new ArrayList<BigInteger>();

        for (int i = 0; i < chunkCount; i++)
        {
            int offset = i * CHUNK_SIZE;
            Byte[] chunk = Arrays.stream(pInputBytesPlusTrim).skip(offset).limit(CHUNK_SIZE).toArray(Byte[]::new);

            BigInteger output = encryptChunk(pKey, chunk);
            values.add(output);
        }
        if (extraBytes != 0)
        {
            Byte[] chunk = Stream.of(pInputBytesPlusTrim).skip(chunkCount * CHUNK_SIZE).limit(extraBytes).toArray(Byte[]::new);
            Byte[] paddedChunk = new Byte[16];
            Arrays.fill(paddedChunk, (byte)0);
            System.arraycopy(chunk, 0, paddedChunk, 0, extraBytes);
            BigInteger output = encryptChunk(pKey, paddedChunk);
            values.add(output);
        }

        ArrayList<Byte> retVal = new ArrayList<>();
        for (BigInteger x : values)
        {
            byte[] bytes = x.toByteArray();
            byte[] out = CryptographyUtilities.PadToMultipleOf(bytes, 17);
            for (int i = 0; i < out.length; i++)
            {
                retVal.add(out[i]);
            }
        }
        Byte[] ret = new Byte[retVal.size()];
        retVal.toArray(ret);

        return ret;
    }

    private static BigInteger encryptChunk(ModulusTreeKey pKey, Byte[] pChunk)
    {
        //Unbox the Byte[]
        int i = 0;
        byte[] bytes = new byte[pChunk.length];
        for (Byte b : pChunk)
        {
            bytes[i++] = b;
        }

        BigInteger input = CryptographyUtilities.bytesToPosBigInteger(bytes);
        return ModulusEncryption.getIndex(pKey, input);
    }
}
