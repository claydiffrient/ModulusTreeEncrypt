package edu.byui.cs480.modulustree;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by clay on 5/17/14.
 */
public class ModulusFileDecryption
{
    private static final int CHUNK_SIZE = 17;

    public static Byte[] decryptBytes(ModulusTreeKey pKey, Byte[] pInputBytes)
    {
        //Need to strip the padding length byte from the beginning.
        int paddingAmount = (int) pInputBytes[0];

        //Now that we have that we need to remove it from our array.
        Byte[] newInput = new Byte[pInputBytes.length - 1];
        for (int i = 1; i < newInput.length - 1; i++)
        {
            newInput[i] = pInputBytes[i];
        }



        int chunkCount = newInput.length / CHUNK_SIZE;
        int extraBytes = newInput.length % CHUNK_SIZE;

        List<BigInteger> values = new ArrayList<BigInteger>();

        for (int i = 0; i < chunkCount; i++)
        {
            int offset = i * CHUNK_SIZE;
            Byte[] chunk = Stream.of(newInput).skip(offset).limit(CHUNK_SIZE).toArray(Byte[]::new);
            BigInteger output = decryptChunk(pKey, chunk);
            values.add(output);
        }
        if (extraBytes != 0)
        {
            System.out.println("This is the extra bytes block.");
            System.out.println(extraBytes);
            Byte[] chunk = Stream.of(newInput).skip(chunkCount * CHUNK_SIZE).limit(extraBytes).toArray(Byte[]::new);
            Byte[] withoutPadding = CryptographyUtilities.stripPadding(chunk, paddingAmount);
            BigInteger output = decryptChunk(pKey, withoutPadding);
            values.add(output);
        }

        return values.stream().flatMap(x -> {
            byte[] bytes = x.toByteArray();
            //Box it up
            int i = 0;
            Byte[] byteObjs = new Byte[bytes.length];
            for (byte b: bytes)
            {
                byteObjs[i++] = b;
            }
            byte last = bytes[bytes.length - 1];
            if (last == 0 || last == 1 ) //TODO: Potential bug! How to decide when to chop off the sign bit?
            {
                byteObjs = Stream.of(bytes).limit(bytes.length - 1).toArray(Byte[]::new);
            }
            return Stream.of(byteObjs);
        }).toArray(Byte[]::new);
    }

    private static BigInteger decryptChunk(ModulusTreeKey pKey, Byte[] pChunk)
    {
        //Unbox
        int i = 0;
        byte[] bytes = new byte[pChunk.length];
        for (Byte b : pChunk)
        {
            bytes[i++] = b;
        }
        BigInteger input = new BigInteger(bytes);
        return ModulusDecryption.getValueAtIndex(pKey, input);
    }
}
