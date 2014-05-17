package edu.byui.cs480.modulustree;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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



    }

    private static void roundTripTests(ModulusTreeKey pKey, int pTestCount)
    {
        Random r = new Random();
        byte[] bytes;

        for (int i = 0; i < pTestCount; i++)
        {
            bytes = new byte[15];
            r.nextBytes(bytes);
            //bytes = CryptographyUtilities.EnsurePositive(bytes);

            //EnsurePositive wasn't working here, so I replaced it with this semi-magical loop.
            BigInteger input = BigInteger.ZERO;
            for (int j = 0; j < bytes.length; j++)
            {
                input = (input.shiftLeft(8)).add(new BigInteger(Integer.toString(bytes[j] & 0xff)));
            }
            //BigInteger input = new BigInteger(bytes);
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


}