package edu.byui.cs480.modulustree;

import com.sun.tools.javac.util.Pair;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by clay on 5/16/14.
 */
public class ModulusEncryption
{
    public static BigInteger getIndex(ModulusTreeKey pKey, BigInteger pValue)
    {
        pValue = pValue.add(BigInteger.ONE);
        if (pValue.compareTo(BigInteger.ONE) == 0)
        {
            return BigInteger.ZERO;
        }
        List<BigInteger> path = getPathToValue(pKey.getmLevels(), pValue);
        List<Pair<Level, BigInteger>> levels = new ArrayList<Pair<Level, BigInteger>>();
        for (int i = 0; i < path.size(); i++)
        {
            levels.add(new Pair<Level, BigInteger>(pKey.getmLevels().get(i), path.get(i)));
        }
        return getIndex(levels, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
    }

    public static BigInteger getIndex(
            List<Pair<Level, BigInteger>> levels,
            BigInteger baseValue,
            BigInteger eol,
            BigInteger previousMod,
            BigInteger previousSkipped)
    {
        if (levels.size() == 0)
        {
            return baseValue;
        }
        Pair<Level, BigInteger> levelInfo = levels.get(0);

        Level level = levelInfo.fst;
        BigInteger currentMod = levelInfo.snd;
        BigInteger nodeCount = level.getNodeCount();
        BigInteger currentPrime = level.getPrime();

        BigInteger skip = previousMod.add(previousSkipped).multiply(currentPrime);
        BigInteger index = baseValue.add(eol).add(skip).add(currentMod).add(BigInteger.ONE);

        BigInteger levelIndex = index.subtract(level.getTreeSizeOfPreviousLevel());
        return getIndex(
                levels.stream().skip(1).collect(Collectors.toList()),
                index,
                nodeCount.subtract(levelIndex).subtract(BigInteger.ONE),
                currentMod,
                skip
        );

    }

    public static List<BigInteger> getPathToValue(List<Level> pLevels, BigInteger pValue)
    {
        Level maxLevel = Level.getLevelContainingValue(pLevels, pValue);
        List<BigInteger> path = new ArrayList<BigInteger>();
        for (int i = 0; i <= maxLevel.getIndex(); i++)
        {
            Level level = pLevels.get(i);
            path.add(pValue.mod(level.getPrime()));
        }
        return path;
    }
}
