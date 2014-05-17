package edu.byui.cs480.modulustree;

import com.sun.org.apache.xml.internal.security.encryption.EncryptedType;

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
        int chunkCount = pInputBytes.length / CHUNK_SIZE;
        int extraBytes = pInputBytes.length % CHUNK_SIZE;

        List<BigInteger> values = new ArrayList<BigInteger>();

        for (int i = 0; i < chunkCount; i++)
        {
            int offset = i * CHUNK_SIZE;
            Byte[] chunk = Arrays.stream(pInputBytes).skip(offset).limit(CHUNK_SIZE).toArray(Byte[]::new);

            BigInteger output = encryptChunk(pKey, chunk);
            values.add(output);
        }
        return values.stream().flatMap( x -> {
                                            byte[] bytes = x.toByteArray();
                                            return Stream.of(CryptographyUtilities.PadToMultipleOf(bytes, 17));
        }).toArray(Byte[]::new);


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
