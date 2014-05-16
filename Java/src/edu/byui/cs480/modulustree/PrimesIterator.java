package edu.byui.cs480.modulustree;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Handles generation of prime numbers.
 *
 * Based on the code at http://epsilon.eu.org/epsilon/index.php?/eng/Computers/Lazy-prime-numbers-iterator-in-Java
 * with slight modifications/additions by Clay Diffrient.
 *
 */
public class PrimesIterator
        implements Iterator<BigInteger>
{
    static private ArrayList<BigInteger> cPrimes = new ArrayList<BigInteger>();
    private ListIterator<BigInteger> mPrimesIter;

    PrimesIterator()
    {
        mPrimesIter = cPrimes.listIterator();
        if (!mPrimesIter.hasNext())
        {
            mPrimesIter.add(new BigInteger("2"));
            mPrimesIter.add(new BigInteger("3"));
            mPrimesIter.previous();
            mPrimesIter.previous();
        }
    }

    @Override
    public boolean hasNext()
    {
        return true;  // There's always another prime
    }

    private static boolean isPrime(BigInteger pArg)
    {
        BigInteger limit = sqrt(pArg);
        BigInteger temp;
        ListIterator<BigInteger> tempPrimeIter = cPrimes.listIterator();
        while (tempPrimeIter.hasNext())
        {
            temp = tempPrimeIter.next();
            if (temp.compareTo(limit) == 1)
            {
                return true;
            }
            if (pArg.mod(temp).equals(BigInteger.ZERO))
            {
                return false;
            }
        }
        return true;
    }

    private void computeNext()
    {
        BigInteger temp;
        temp = mPrimesIter.previous().add(new BigInteger("2"));
        mPrimesIter.next();
        while (!PrimesIterator.isPrime(temp))
        {
            temp = temp.add(BigInteger.ONE);
        }
        mPrimesIter.add(temp);
        mPrimesIter.previous();
    }

    @Override
    public BigInteger next()
    {
        if (!mPrimesIter.hasNext())
        {
            this.computeNext();
        }
        return mPrimesIter.next();
    }

    /**
     * Calculates the square root of a BigInteger.
     *
     * Originally from http://faruk.akgul.org/blog/javas-missing-algorithm-biginteger-sqrt/
     * Slightly modified by Clay Diffrient
     */
    private static BigInteger sqrt(BigInteger pNum)
    {
        BigInteger a = BigInteger.ONE;
        BigInteger b = new BigInteger(pNum.shiftRight(5).add(new BigInteger("8")).toString());
        while (b.compareTo(a) >= 0)
        {
            BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
            if (mid.multiply(mid).compareTo(pNum) > 0)
            {
                b = mid.subtract(BigInteger.ONE);
            }
            else
            {
                a = mid.add(BigInteger.ONE);
            }
        }
        return a.subtract(BigInteger.ONE);
    }
}



