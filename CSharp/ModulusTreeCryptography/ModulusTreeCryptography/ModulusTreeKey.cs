using System.Collections.Generic;
using System.Linq;
using System.Numerics;

namespace ModulusTreeCryptography
{
    public class ModulusTreeKey
    {
        public List<Level> Levels { get; private set; }

        public ModulusTreeKey(IEnumerable<BigInteger> primes)
        {
            this.Levels = new List<Level>();

            var keys = primes.ToList();
            BigInteger count = 1;
            for (int i = 0; i < keys.Count; i++)
            {
                var currentKey = keys[i];
                var groupCount = count;
                count *= currentKey;

                var previousCounts = Levels.Take(i).Select(l => l.NodeCount).ToList();
                BigInteger treeSizeOfPreviousLevel = 1;
                if (previousCounts.Any())
                    treeSizeOfPreviousLevel += previousCounts.Aggregate((x, y) => x + y);

                var level = new Level
                {
                    NodeCount = count,
                    Prime = currentKey,
                    Index = i + 1,
                    NodesPerGroup = currentKey,
                    GroupCount = groupCount,
                    TreeSizeOfPreviousLevel = treeSizeOfPreviousLevel
                };
                this.Levels.Add(level);
            }


            BigInteger valueCount = 1;
            foreach (var level in this.Levels)
            {
                level.MinValue = valueCount + 1;
                level.MaxValue = valueCount = level.NodeCount + valueCount;
            }
        }
    }
}
