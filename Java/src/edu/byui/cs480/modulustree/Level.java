package edu.byui.cs480.modulustree;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by clay on 5/16/14.
 */
public class Level
{
    public BigInteger Prime;
    public BigInteger NodesPerGroup;
    public BigInteger GroupCount;
    public BigInteger TreeSizeOfPreviousLevel;
    public BigInteger NodeCount;
    public BigInteger MinValue;
    public BigInteger MaxValue;
    public int Index;

    public BigInteger getPrime() {
        return Prime;
    }

    public void setPrime(BigInteger prime) {
        Prime = prime;
    }

    public BigInteger getNodesPerGroup() {
        return NodesPerGroup;
    }

    public void setNodesPerGroup(BigInteger nodesPerGroup) {
        NodesPerGroup = nodesPerGroup;
    }

    public BigInteger getGroupCount() {
        return GroupCount;
    }

    public void setGroupCount(BigInteger groupCount) {
        GroupCount = groupCount;
    }

    public BigInteger getTreeSizeOfPreviousLevel() {
        return TreeSizeOfPreviousLevel;
    }

    public void setTreeSizeOfPreviousLevel(BigInteger treeSizeOfPreviousLevel) {
        TreeSizeOfPreviousLevel = treeSizeOfPreviousLevel;
    }

    public BigInteger getNodeCount() {
        return NodeCount;
    }

    public void setNodeCount(BigInteger nodeCount) {
        NodeCount = nodeCount;
    }

    public BigInteger getMinValue() {
        return MinValue;
    }

    public void setMinValue(BigInteger minValue) {
        MinValue = minValue;
    }

    public BigInteger getMaxValue() {
        return MaxValue;
    }

    public void setMaxValue(BigInteger maxValue) {
        MaxValue = maxValue;
    }

    public int getIndex() {
        return Index;
    }

    public void setIndex(int index) {
        Index = index;
    }

    public static Level getLevelContainingIndex(List<Level> pLevels, BigInteger pIndex)
    {
        for (Level l : pLevels)
        {
            if (l.getMinValue().subtract(BigInteger.ONE).compareTo(pIndex) < 0 &&
                    l.getMaxValue().subtract(BigInteger.ONE).compareTo(pIndex) > 0)
            {
                return l;
            }
        }
        return null; // Should never get here, we hope :)
    };


    public static Level getLevelContainingValue(List<Level> pLevels, BigInteger pValue)
    {
        for (Level l : pLevels)
        {
            if (l.getMinValue().compareTo(pValue) <= 0 &&
                    l.getMaxValue().compareTo(pValue) >= 0)
            {
                return l;
            }
        }
        return null; // Should never get here, we hope :)
    };
}
