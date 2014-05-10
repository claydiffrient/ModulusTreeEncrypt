using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;

namespace ModulusTreeCryptography
{
    public static class ModulusFileEncryption
    {
        private const int chunkSize = 16;

        public static byte[] EncryptBytes(ModulusTreeKey key, byte[] inputBytes)
        {
            var chunkCount = inputBytes.Length / chunkSize;
            var extraBytes = inputBytes.Length % chunkSize;

            var values = new List<BigInteger>();

            for (int i = 0; i < chunkCount; i++)
            {
                var offset = i * chunkSize;
                var chunk = inputBytes.Skip(offset).Take(chunkSize).ToArray();

                var output = EncryptChunk(key, chunk);
                values.Add(output);
            }

            if (extraBytes != 0)
            {
                var chunk = inputBytes.Skip(chunkCount * chunkSize).Take(extraBytes).ToArray();

                var output = EncryptChunk(key, chunk);
                values.Add(output);
            }

            return values.SelectMany(x =>
                                     {
                                         var bytes = x.ToByteArray();
                                         CryptographyUtilities.PadToMultipleOf(ref bytes, 17);
                                         return bytes;
                                     }).ToArray();
        }

        private static BigInteger EncryptChunk(ModulusTreeKey key, byte[] chunk)
        {
            CryptographyUtilities.EnsurePositive(ref chunk);
            var input = new BigInteger(chunk);
            var output = ModulusEncryption.GetIndex(key, input);
            return output;
        }
    }
}
