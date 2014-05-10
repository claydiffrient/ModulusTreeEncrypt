using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace ModulusTreeCryptography
{
    public static class ModulusDecryption
    {
        public static BigInteger GetValueAtIndex(ModulusTreeKey key, BigInteger treeIndex)
        {
            if (treeIndex == 0)
                return 1;

            var modulusPath = key.GetModulusPathToIndex(treeIndex).ToList();
            var value = key.GetValueByModulusPath(modulusPath);
            var level = key.Levels[modulusPath.Count - 1];
            var size = key.Primorials[level.Prime];
            while (value < level.MinValue)
            {
                value += size;
            }
            return --value;
        }

        public static BigInteger GetValueByModulusPath(this ModulusTreeKey key, IEnumerable<BigInteger> modulusPath) // modulusPath: 0, 1, 3, 4
        {
            var path = modulusPath.ToList();
            var crtValues = key.Primes
                .Zip(path, CRTValues.Create)
                .Take(path.Count)
                .ToList();

            var value = CRT.ChineseRemainder(
                crtValues.Select(v => v.Remainder).ToList(),
                crtValues.Select(v => v.Modulus).ToList());

            return value;
        }

        public static IEnumerable<BigInteger> GetModulusPathToIndex(this ModulusTreeKey key, BigInteger treeIndex)
        {
            var level = key.GetLevelContainingIndex(treeIndex);

            var groupInfos = new List<GroupInfo>();

            var currentValue = key.GetGroupInfoForIndex(treeIndex);
            groupInfos.Add(currentValue);

            var index = level.Index;
            for (int i = index; i > 0; i--)
            {
                var currentLevel = key.Levels[i - 1];
                var newValue = key.GetGroupInfoForIndexOnLevel(currentLevel, currentValue.GroupIndex);
                groupInfos.Add(newValue);
                currentValue = newValue;
            }

            var modulusPath = new List<BigInteger>();
            modulusPath.AddRange(groupInfos.Select(gi => gi.IndexInGroup));
            modulusPath.Reverse();
            return modulusPath;
        }

        private static GroupInfo GetGroupInfoForIndex(this ModulusTreeKey key, BigInteger treeIndex)
        {
            var level = key.GetLevelContainingIndex(treeIndex);
            var parentTreeSize = level.MinValue - 1; // 9
            var levelIndex = treeIndex - parentTreeSize; // 22 - 9 = 13
            return key.GetGroupInfoForIndexOnLevel(level, levelIndex);
        }

        private static GroupInfo GetGroupInfoForIndexOnLevel(this ModulusTreeKey key, Level level, BigInteger levelIndex)
        {
            BigInteger indexInGroup; // 13 % 5 = 3
            var groupIndex = BigInteger.DivRem(levelIndex, level.NodesPerGroup, out indexInGroup); // 13 / 5 = 2

            return new GroupInfo(groupIndex, indexInGroup);
        }
        
        class GroupInfo
        {
            public GroupInfo(BigInteger groupIndex, BigInteger indexInGroup)
            {
                this.GroupIndex = groupIndex;
                this.IndexInGroup = indexInGroup;
            }

            public BigInteger GroupIndex { get; private set; }
            public BigInteger IndexInGroup { get; private set; }
        }

        class CRTValues
        {
            public static CRTValues Create(BigInteger modulus, BigInteger remainder)
            {
                var crtValue = new CRTValues
                               {
                                   Modulus = modulus, 
                                   Remainder = remainder
                               };
                return crtValue;
            }

            public BigInteger Modulus { get; set; }
            public BigInteger Remainder { get; set; }
        }
    }
}
