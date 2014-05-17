package edu.byui.cs480.modulustree;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by clay on 5/16/14.
 */
public class ModulusDecryption
{
    public static BigInteger getValueAtIndex(ModulusTreeKey pKey, BigInteger pTreeIndex)
    {
        if (pTreeIndex.compareTo(BigInteger.ZERO) == 0)
        {
            return BigInteger.ONE;
        }
        List<BigInteger> modulusPath = getModulusPathToIndex(pKey, pTreeIndex);
        BigInteger value = getValueByModulusPath(pKey, modulusPath);
        Level level = pKey.getmLevels().get(modulusPath.size() - 1);
        BigInteger size = pKey.getmPrimorials().get(level.getPrime());
        while (value.compareTo(level.getMinValue()) == -1)
        {
            value = value.add(BigInteger.ONE);
        }
        return value.subtract(BigInteger.ONE);
    }

    public static BigInteger getValueByModulusPath(ModulusTreeKey pKey, List<BigInteger> pModulusPath)
    {
        List<BigInteger> path = pModulusPath;
        List<CRTValues> crtValues = Utilities.zip(pKey.getmPrimes().stream(),
                                                   path.stream(),
                                                   (prime, pathVal) -> new CRTValues(prime, pathVal)).
                                                   limit(path.size()).
                                                   collect(Collectors.toList());
        try
        {
            BigInteger[] value = BigIntegerMath.solveCRT(
                    crtValues.stream().map(v -> v.getRemainder()).toArray(BigInteger[]::new),
                    crtValues.stream().map(v -> v.getModulus()).toArray(BigInteger[]::new));

//            BigInteger value = CRT.ChineseRemainder(
//                    crtValues.stream().map(v -> v.getRemainder()).collect(Collectors.toList()),
//                    crtValues.stream().map(v -> v.getModulus()).collect(Collectors.toList()));
            return value[0];
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public static List<BigInteger> getModulusPathToIndex(ModulusTreeKey pKey, BigInteger pTreeIndex)
    {
        Level level = Level.getLevelContainingIndex(pKey.getmLevels(), pTreeIndex);
        List<GroupInfo> groupInfos = new ArrayList<GroupInfo>();

        GroupInfo currentValue = getGroupInfoForIndex(pKey, pTreeIndex);
        groupInfos.add(currentValue);
        int index = level.getIndex();
        for (int i = index; i > 0; i--)
        {
            Level currentLevel = pKey.getmLevels().get(i - 1);
            GroupInfo newValue = getGroupInfoForIndexOnLevel(pKey, currentLevel, currentValue.getGroupIndex());
            groupInfos.add(newValue);
            currentValue = newValue;
        }
        List<BigInteger> modulusPath = new ArrayList<BigInteger>();
        modulusPath.addAll(groupInfos.stream().
                                      map(gi -> gi.getIndexInGroup()).
                                      collect(Collectors.toList()));
        Collections.reverse(modulusPath);
        return modulusPath;
    }

    public static GroupInfo getGroupInfoForIndex(ModulusTreeKey pKey, BigInteger pTreeIndex)
    {
        Level level = Level.getLevelContainingIndex(pKey.getmLevels(), pTreeIndex);
        BigInteger parentTreeSize = level.getMinValue().subtract(BigInteger.ONE);
        BigInteger levelIndex = pTreeIndex.subtract(parentTreeSize);
        return getGroupInfoForIndexOnLevel(pKey, level, levelIndex);

    }

    public static GroupInfo getGroupInfoForIndexOnLevel(ModulusTreeKey pKey, Level pLevel, BigInteger pLevelIndex)
    {
        BigInteger indexInGroup;
        BigInteger[] groupIndex = pLevelIndex.divideAndRemainder(pLevel.getNodesPerGroup());
        return new GroupInfo(groupIndex[0], groupIndex[1]);
    }

    private static class GroupInfo
    {
        private BigInteger mGroupIndex;
        private BigInteger mIndexInGroup;

        public BigInteger getGroupIndex() {
            return mGroupIndex;
        }

        public void setGroupIndex(BigInteger pGroupIndex) {
            mGroupIndex = pGroupIndex;
        }

        public BigInteger getIndexInGroup() {
            return mIndexInGroup;
        }

        public void setIndexInGroup(BigInteger pIndexInGroup) {
            mIndexInGroup = pIndexInGroup;
        }

        public GroupInfo(BigInteger pGroupIndex, BigInteger pIndexInGroup)
        {
            mGroupIndex = pGroupIndex;
            mIndexInGroup = pIndexInGroup;
        }
    }

    private static class CRTValues
    {
        private BigInteger mModulus;
        private BigInteger mRemainder;

        public BigInteger getRemainder() {
            return mRemainder;
        }

        public void setRemainder(BigInteger pRemainder) {
            mRemainder = pRemainder;
        }

        public BigInteger getModulus() {
            return mModulus;
        }

        public void setModulus(BigInteger pModulus) {
            mModulus = pModulus;
        }



        public CRTValues(BigInteger pModulus, BigInteger pRemainder)
        {
            mModulus = pModulus;
            mRemainder = pRemainder;
        }
    }
}
