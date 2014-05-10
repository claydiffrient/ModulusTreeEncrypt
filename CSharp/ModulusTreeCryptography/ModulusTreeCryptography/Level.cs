using System.Numerics;

namespace ModulusTreeCryptography
{
    public class Level
    {
        public BigInteger Prime { get; set; }
        public BigInteger NodesPerGroup { get; set; }
        public BigInteger GroupCount { get; set; }
        public BigInteger TreeSizeOfPreviousLevel { get; set; }
        public BigInteger NodeCount { get; set; }
        public BigInteger MinValue { get; set; }
        public BigInteger MaxValue { get; set; }
        public int Index { get; set; }
    }
}
