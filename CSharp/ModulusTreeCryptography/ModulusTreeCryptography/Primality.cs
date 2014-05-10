using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;

namespace ModulusTreeCryptography
{
    public static class Primality
    {
        public static int GetGCDByModulus(int value1, int value2)
        {
            while (value1 != 0 && value2 != 0)
            {
                if (value1 > value2)
                    value1 %= value2;
                else
                    value2 %= value1;
            }
            return Math.Max(value1, value2);
        }

        public static bool Coprime(int value1, int value2)
        {
            return GetGCDByModulus(value1, value2) == 1;
        }

        public static IEnumerable<int> GetRelativePrimes(int seed)
        {
            yield return seed;

            for (int i = seed + 1; ; i++)
            {
                if (Coprime(seed, i))
                    yield return i;
            }
        }
        public static IEnumerable<BigInteger> Primes()
        {
            var memoized = new List<BigInteger>();
            BigInteger sqrt = 1;
            var primes = PotentialPrimes().Where(x =>
            {
                sqrt = GetSqrtCeiling(x, sqrt);
                return memoized.TakeWhile(y => y <= sqrt).All(y => x % y != 0);
            });
            foreach (int prime in primes)
            {
                yield return prime;
                memoized.Add(prime);
            }
        }

        private static IEnumerable<BigInteger> PotentialPrimes()
        {
            yield return 2;
            yield return 3;
            BigInteger k = 1;
            while (k > 0)
            {
                yield return 6 * k - 1;
                yield return 6 * k + 1;
                k++;
            }
        }

        private static BigInteger GetSqrtCeiling(BigInteger value, BigInteger start)
        {
            while (start * start < value)
            {
                start++;
            }
            return start;
        }
    }
}
