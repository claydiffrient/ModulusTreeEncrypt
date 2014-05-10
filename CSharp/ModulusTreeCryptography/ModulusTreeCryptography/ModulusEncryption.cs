using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;

namespace ModulusTreeCryptography
{
    public static class ModulusEncryption
    {
        public static BigInteger GetIndex(ModulusTreeKey key, BigInteger value)
        {
            value++;

            if (value == 1)
                return 0;

            var path = GetPathToValue(key.Levels, value);
            var levels = path.Select((mod, i) => Tuple.Create(key.Levels[i], mod)).ToList();
            return GetIndex(levels, 0, 0, 0, 0);
        }

        public static BigInteger GetIndex(
            List<Tuple<Level, BigInteger>> levels,
            BigInteger baseIndex,
            BigInteger eol,
            BigInteger previousMod,
            BigInteger previousSkipped)
        {
            var levelInfo = levels.FirstOrDefault();
            if (levelInfo == null)
                return baseIndex;

            var level = levelInfo.Item1;
            var currentMod = levelInfo.Item2;
            var nodeCount = level.NodeCount;
            var currentPrime = level.Prime;

            var skip = (previousMod + previousSkipped) * currentPrime;
            var index = baseIndex + eol + skip + currentMod + 1;

            var levelIndex = index - level.TreeSizeOfPreviousLevel;
            return GetIndex(
                levels.Skip(1).ToList(),
                index,
                nodeCount - levelIndex - 1,
                currentMod,
                skip);
        }


        public static IEnumerable<BigInteger> GetPathToValue(IEnumerable<Level> levels, BigInteger value)
        {
            var currentLevels = levels.ToList();
            var maxLevel = currentLevels.GetLevelContainingValue(value);
            return currentLevels
                       .Select(level => value % level.Prime)
                       .Take(maxLevel.Index + 1)
                       .ToList();
        }
    }
}
