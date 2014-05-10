using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using Newtonsoft.Json;

namespace ModulusTreeCryptography
{
    class Program
    {
        static void Main(string[] args)
        {
            var key = new ModulusTreeKey(Primality.Primes().Take(28).ToList());

            TestEncryption(key);
            Console.WriteLine();
            TestDecryption(key);

            Console.ReadLine();
        }

        private static void TestDecryption(ModulusTreeKey key)
        {
            var inputs = Enumerable.Range(0, 41).Select(x => new BigInteger(x)).ToList();
            var outputs = new List<BigInteger> { 1, 2, 3, 6, 4, 8, 9, 7, 5, 30, 36, 12, 18, 24, 10, 16, 22, 28, 34, 20, 26, 32, 38, 14, 15, 21, 27, 33, 39, 25, 31, 37, 13, 19, 35, 11, 17, 23, 29, 210, 120 };
            Console.WriteLine("Decryption Tests:");
            for (int i = 0; i < 41; i++)
            {
                var input = inputs[i];
                var expectedOutput = outputs[i];
                var result = ModulusDecryption.GetValueAtIndex(key, input);
                Console.WriteLine("Input: {0}, Expected Output: {1}, Actual Output: {2}", input, expectedOutput, result);
                if (result != expectedOutput)
                    throw new Exception("Values are not equal!");
            }
        }

        static void TestEncryption(ModulusTreeKey key)
        {
            var outputs = Enumerable.Range(0, 41).Select(x => new BigInteger(x)).ToList();
            var inputs = new List<BigInteger> { 1, 2, 3, 6, 4, 8, 9, 7, 5, 30, 36, 12, 18, 24, 10, 16, 22, 28, 34, 20, 26, 32, 38, 14, 15, 21, 27, 33, 39, 25, 31, 37, 13, 19, 35, 11, 17, 23, 29, 210, 120 };
            Console.WriteLine("Encryption Tests:");
            for (int i = 0; i < 41; i++)
            {
                var input = inputs[i];
                var expectedOutput = outputs[i];
                var result = ModulusEncryption.GetIndex(key, input);
                Console.WriteLine("Input: {0}, Expected Output: {1}, Actual Output: {2}", input, expectedOutput, result);
                if (result != expectedOutput)
                    throw new Exception("Values are not equal!");
            }
        }
    }
}
