using System;
using System.Collections.Generic;
using System.IO;
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

            //TestEncryption(key);
            //Console.WriteLine();
            //TestDecryption(key);
            //Console.WriteLine();
            RoundTripTests(key, 100);
            //RoundTripByteTests(key, 10);

            Console.WriteLine("Encrypting...");
            TestFileEncryption(key, "testfile2", "txt");
            Console.WriteLine("Decrypting...");
            TestFileDecryption(key, "testfile2", "txt");
            Console.WriteLine("Completed!");

            Console.ReadLine();
        }

        private static void RoundTripTests(ModulusTreeKey key, int testCount)
        {
            Random r = new Random();
            byte[] bytes;

            for (int i = 0; i < testCount; i++)
            {
                bytes = new byte[15];
                r.NextBytes(bytes);
                CryptographyUtilities.EnsurePositive(ref bytes);

                BigInteger input = new BigInteger(bytes);
                var encrypted = ModulusEncryption.GetIndex(key, input);
                var decrypted = ModulusDecryption.GetValueAtIndex(key, encrypted);
                Console.WriteLine("Input: {0}, Encrypted: {1}, Decrypted: {2}", input, encrypted, decrypted);
                if (decrypted != input)
                    throw new Exception("Values are not equal!");

            }

            Console.WriteLine("Completed!");
        }

        private static void RoundTripByteTests(ModulusTreeKey key, int testCount)
        {
            Random r = new Random();
            byte[] bytes;

            for (int i = 0; i < testCount; i++)
            {
                bytes = new byte[8];
                r.NextBytes(bytes);

                var encrypted = ModulusFileEncryption.EncryptBytes(key, bytes);
                var decrypted = ModulusFileDecryption.DecryptBytes(key, encrypted);
                if (!decrypted.SequenceEqual(bytes))
                    throw new Exception("Values are not equal!");

            }

            Console.WriteLine("Completed!");
        }


        private static void TestByteArrayEncryption(ModulusTreeKey key)
        {
            var bytes = new List<byte>(Enumerable.Repeat((byte)0, 15));
            bytes.Add(210);
            var result = ModulusFileEncryption.EncryptBytes(key, bytes.ToArray());
            Console.WriteLine("File Encryption Tests:");
            Console.WriteLine("Input: {0}, Expected Output: {1}, Actual Output: {2}", 210, 39, JsonConvert.SerializeObject(result.Select(b => b)));
        }

        private static void TestFileEncryption(ModulusTreeKey key, string fileName, string extension)
        {
            var bytes = File.ReadAllBytes(string.Format("{0}.{1}", fileName, extension));
            var result = ModulusFileEncryption.EncryptBytes(key, bytes.ToArray());
            File.WriteAllBytes(string.Format("{0}.encrypted", fileName), result);
        }

        private static void TestFileDecryption(ModulusTreeKey key, string fileName, string extension)
        {
            var bytes = File.ReadAllBytes(string.Format("{0}.encrypted", fileName));
            var result = ModulusFileDecryption.DecryptBytes(key, bytes.ToArray());
            File.WriteAllBytes(string.Format("{0}.decrypted.{1}", fileName, extension), result);
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
