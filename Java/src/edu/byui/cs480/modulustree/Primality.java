package edu.byui.cs480.modulustree;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Primality
{
   public static int getGCDByModulus(int pValue1, int pValue2)
   {
      while (pValue1 != 0 && pValue2 != 0)
      {
         if (pValue1 > pValue2)
         {
            pValue1 %= pValue2;
         }
         else
         {
            pValue2 %= pValue1;
         }
      }
      return Math.max(pValue1, pValue2);
   }

   public static boolean isCoprime(int pValue1, int pValue2)
   {
      return getGCDByModulus(pValue1, pValue2) == 1;
   }

   public static Iterable<Integer> getRelativePrimes(final int pSeed)
   {
      return new Iterable<Integer>()
      {
          int seed = pSeed;
          @Override
          public Iterator<Integer> iterator()
          {
              return new Iterator<Integer>()
              {
                  @Override
                  public boolean hasNext()
                  {
                      return true;
                  }

                  @Override
                  public Integer next()
                  {
                      for (int i = seed + 1; ; i++)
                      {
                          if (isCoprime(seed, i))
                          {
                              return i;
                          }
                      }
                  }
              };
          }
      };
   }

    public static Iterable<BigInteger> getPrimes()
    {
        return new Iterable<BigInteger>()
        {
            List<BigInteger> memoized = new ArrayList<BigInteger>();
            BigInteger sqrt = BigInteger.ONE;
            Iterable<BigInteger> primes = getPotentialPrimes()
            @Override
            public Iterator<BigInteger> iterator()
            {
                return new Iterator<BigInteger>()
                {
                    @Override
                    public boolean hasNext()
                    {
                        return true;
                    }

                    @Override
                    public BigInteger next()
                    {
                        return null;
                    }
                };
            }
        };
    }
    public static Iterable<BigInteger> getPotentialPrimes()
    {
        int runCount = 0;
        return new Iterable<BigInteger>()
        {
            @Override
            public Iterator<BigInteger> iterator()
            {
                return new Iterator<BigInteger>()
                {
                    @Override
                    public boolean hasNext()
                    {
                        return true;
                    }

                    @Override
                    public BigInteger next()
                    {
                        runCount++;
                        if (runCount == 0)
                        {
                            return new BigInteger("2");
                        }
                        if (runCount == 1)
                        {
                            return new BigInteger("3");
                        }
                        BigInteger k = BigInteger.ONE;
                        BigInteger one = BigInteger.ONE;
                        BigInteger six = new BigInteger("6");
                        while (k.compareTo(BigInteger.ZERO) == 1)
                        {
                            //Kinda a hacky way to make sure this is a copy.
                            BigInteger curK = k.add(BigInteger.ZERO);
                            k = k.add(one);
                            if (runCount % 2 != 0)
                            {
                                return k.multiply(six).subtract(one);
                            }
                            else
                            {
                                return k.multiply(six).add(one);
                            }
                        }
                        return null;
                    };
                };
            }
        };
    }

}