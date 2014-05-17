package edu.byui.cs480.modulustree;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by clay on 5/16/14.
 */
/// <summary>
/// Solve Chinese Remainder Problem
/// </summary>
public class CRT
{
    /******************************************************************************
     * solves the set of equations using the Chinese Remainder Theorem
     * source: (modified from original to use the ZZ library instead)
     http://forums.justlinux.com/showthread.php?47863-Chinese-Remainder-Theorem-in-C
     *
     * Example:
     * vector c would be filled w/: 2, 3, 4, and 5
     * vector m would be filled w/: 3, 5, 11, and 16
     *
     * then this method computes:
     * "z = 2 mod 3"
     * "z = 3 mod 5"
     * "z = 4 mod 11"
     * "z = 5 mod 16"
     *
     * the result for this system would be "1973"
     ******************************************************************************/
    public static BigInteger ChineseRemainder(List<BigInteger> c, List<BigInteger> m)
        throws Exception
    {
        if (c.size() == 1 && m.size() == 1)
        {
            return c.get(0).add(m.get(0));
        }

        int i, j;
        BigInteger x = BigInteger.ZERO;
        List<BigInteger> M = new ArrayList<BigInteger>();
        int n = m.size();
        int[] b = new int[m.size()];
        for (i = 0; i < n; ++i)
        {
            if (c.get(i).compareTo(m.get(i)) == -1)
            {
                c.set(i, c.get(i).mod(m.get(i)));
            }
        }

        if (m.size() != c.size())
        {
            throw new Exception("Invalid inputs for the Chinese Remainder! Aborting...");
        }

        for (i = 0; i < n; ++i)
        {
            // Gets M's
            if (i == 0)
            {
                M.add(m.get(1));
                j = 1;
            }
            else
            {
                M.add(m.get(0));
                j = 0;
            }
            while (++j < n)
            {
                if (i != j)
                {
                    M.set(i, M.get(i).multiply(m.get(j)));
                }
            }

        }
        for (i = 0; i < n; ++i)
        {
            // Gets b's
            j = 0;
            while ((M.get(i).multiply(new BigInteger(Integer.toString(++j)))).mod(m.get(i)) != BigInteger.ONE && j > 0)
            {
            }
            if (j < 0)
            {
                throw new Exception("Error while computing the Chinese Remainder! Aborting...\n");
            }
            else
            {
                b[i] = j;
            }
        }
        for (i = 0; i < n; ++i)
        {
            // Finds a number that works
            x.add(c.get(i).multiply(new BigInteger(Integer.toString(b[i]))).multiply(M.get(i)));
        }

        return x.mod(M.get(0).multiply(m.get(0))); // Finds a number less than PI m.
    }
}
