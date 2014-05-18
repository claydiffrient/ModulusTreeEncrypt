package edu.byui.cs480.modulustree;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

public class Run
   implements Runnable
{
    public static void main(String[] args)
    {
        new Run().run();
    }


    @Override
    public void run()
    {
        PrimesIterator primesIterator = new PrimesIterator();
        ArrayList<BigInteger> primes = new ArrayList<BigInteger>();
        for (int i = 0; i < 28; i++)
        {
            primes.add(primesIterator.next());
        }
        ModulusTreeKey key = new ModulusTreeKey(primes);

        //List<BigInteger> vals = ModulusEncryption.getPathToValue(key.getmLevels(), new BigInteger("23"));
        roundTripTests(key, 20);
        System.out.println("======================================================");
        roundTripByteTest(key, 20);



    }

    private static void roundTripTests(ModulusTreeKey pKey, int pTestCount)
    {
        Random r = new Random();
        byte[] bytes;

        for (int i = 0; i < pTestCount; i++)
        {
            bytes = new byte[15];
            r.nextBytes(bytes);
            BigInteger input = CryptographyUtilities.bytesToPosBigInteger(bytes);
            BigInteger encrypted = ModulusEncryption.getIndex(pKey, input);
            BigInteger decrypted = ModulusDecryption.getValueAtIndex(pKey, encrypted);
            System.out.println("Input: " + input + ", Encrypted: " + encrypted + ", Decrypted: " + decrypted);
            if (decrypted.compareTo(input) != 0)
            {
                System.out.println("ERROR: Not Equal");
            }
        }
        System.out.println("Completed.");
    }

    private  static void roundTripByteTest(ModulusTreeKey pKey, int pTestCount)
    {
        Random r = new Random();
        byte[] bytes;
        for (int i = 0; i < pTestCount; i++)
        {
            bytes = new byte[8];
            r.nextBytes(bytes);

            //Boxing things up
            int j = 0;
            Byte[] byteObjs = new Byte[bytes.length];
            for (byte b: bytes)
            {
                byteObjs[j++] = b;
            }

            Byte[] encrypted = ModulusFileEncryption.encryptBytes(pKey, byteObjs);
            Byte[] decrypted = ModulusFileDecryption.decryptBytes(pKey, encrypted);
            if (! Arrays.deepEquals(decrypted, byteObjs))
            {
                System.out.println("ERROR: Not Equal");

                System.out.println(byteObjs.length);
                for (Byte b : byteObjs)
                    System.out.print(b + " ");
                System.out.println();

                System.out.println(decrypted.length);
                for (Byte b : decrypted)
                    System.out.print(b + " ");
                System.out.println();

            }


        }
    }


}