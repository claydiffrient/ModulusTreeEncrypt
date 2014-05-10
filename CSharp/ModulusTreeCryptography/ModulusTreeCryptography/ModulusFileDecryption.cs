using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;

namespace ModulusTreeCryptography
{
    public static class ModulusFileDecryption
    {
        private const int chunkSize = 17;

        public static byte[] DecryptBytes(ModulusTreeKey key, byte[] inputBytes)
        {
            var chunkCount = inputBytes.Length / chunkSize;
            var extraBytes = inputBytes.Length % chunkSize;

            var values = new List<BigInteger>();

            for (int i = 0; i < chunkCount; i++)
            {
                var offset = i * chunkSize;
                var chunk = inputBytes.Skip(offset).Take(chunkSize).ToArray();

                var output = DecryptChunk(key, chunk);
                values.Add(output);
            }

            if (extraBytes != 0)
            {
                var chunk = inputBytes.Skip(chunkCount * chunkSize).Take(extraBytes).ToArray();

                var output = DecryptChunk(key, chunk);
                values.Add(output);
            }

            return values.SelectMany(x =>
                                     {
                                         var bytes = x.ToByteArray();
                                         var last = bytes.Last();
                                         if (last == 0 || last == 1) //TODO: Potential bug! How to decide when to chop off the sign bit?
                                             bytes = bytes.Take(bytes.Length - 1).ToArray();
                                         return bytes;
                                     }).ToArray();
        }

        private static BigInteger DecryptChunk(ModulusTreeKey key, byte[] chunk)
        {
            var input = new BigInteger(chunk);
            var output = ModulusDecryption.GetValueAtIndex(key, input);
            return output;
        }
    }
}
