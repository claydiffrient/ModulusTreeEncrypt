using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;

namespace ModulusTreeCryptography
{
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
        {
            if (c.Count == 1 && m.Count == 1)
            {
                return c.First() + m.First();
            }

            int i, j;
            BigInteger x = 0;
            var M = new List<BigInteger>();
            var n = m.Count;
            var b = new int[m.Count];
            for (i = 0; i < n; ++i)
                if (c[i] < m[i])
                    c[i] %= m[i];

            if (m.Count != c.Count)
            {
                throw new Exception("Invalid inputs for the Chinese Remainder! Aborting...");
            }

            for (i = 0; i < n; ++i)
            {
                // Gets M's
                if (i == 0)
                {
                    M.Add(m[1]);
                    j = 1;
                }
                else
                {
                    M.Add(m[0]);
                    j = 0;
                }
                while (++j < n)
                    if (i != j)
                        M[i] *= m[j];
            }
            for (i = 0; i < n; ++i)
            {
                // Gets b's
                j = 0;
                while ((M[i] * ++j) % m[i] != 1 && j > 0)
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
                x += c[i] * b[i] * M[i]; // Finds a number that works

            return x % (M[0] * m[0]); // Finds a number less than PI m.
        }
    }
}
