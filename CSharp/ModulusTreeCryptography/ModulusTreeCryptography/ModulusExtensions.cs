using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;

namespace ModulusTreeCryptography
{
    public static class ModulusExtensions
    {
        public static Level GetLevelContainingValue(this ModulusTreeKey key, BigInteger value)
        {
            return key.Levels.GetLevelContainingValue(value);
        }

        public static Level GetLevelContainingValue(this IEnumerable<Level> levels, BigInteger value)
        {
            return levels.SingleOrDefault(l => l.MinValue <= value && l.MaxValue >= value);
        }

        public static Level GetLevelContainingIndex(this ModulusTreeKey key, BigInteger index)
        {
            return key.Levels.GetLevelContainingIndex(index);
        }

        public static Level GetLevelContainingIndex(this IEnumerable<Level> levels, BigInteger index)
        {
            return levels.SingleOrDefault(l => l.MinValue - 1 <= index && l.MaxValue - 1 >= index);
        }
    }
}
