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
        System.out.println(ModulusEncryption.getIndex(key, new BigInteger("8")));



    }

    private static void roundTripTests(ModulusTreeKey pKey, int pTestCount)
    {
        Random r = new Random();
        byte[] bytes;

        for (int i = 0; i < pTestCount; i++)
        {
            bytes = new byte[15];
            r.nextBytes(bytes);
        }
    }


}