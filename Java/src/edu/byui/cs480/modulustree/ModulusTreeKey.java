package edu.byui.cs480.modulustree;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by clay on 5/16/14.
 */
public class ModulusTreeKey
{
    private List<BigInteger> mPrimes;
    private List<Level> mLevels;
    private Map<BigInteger, BigInteger> mPrimorials;

    public List<BigInteger> getmPrimes() {
        return mPrimes;
    }

    public void setmPrimes(List<BigInteger> mPrimes) {
        this.mPrimes = mPrimes;
    }

    public List<Level> getmLevels() {
        return mLevels;
    }

    public void setmLevels(List<Level> mLevels) {
        this.mLevels = mLevels;
    }

    public Map<BigInteger, BigInteger> getmPrimorials() {
        return mPrimorials;
    }

    public void setmPrimorials(Map<BigInteger, BigInteger> mPrimorials) {
        this.mPrimorials = mPrimorials;
    }

    public ModulusTreeKey(List<BigInteger> pPrimes)
    {
        mPrimes = pPrimes;
        mLevels = new ArrayList<Level>();
        mPrimorials = new HashMap<BigInteger, BigInteger>();

        BigInteger count = BigInteger.ONE;
        for (int i = 0; i < mPrimes.size(); i++)
        {
            BigInteger currentKey = mPrimes.get(i);
            BigInteger groupCount = count;
            count = count.multiply(currentKey);
            mPrimorials.put(currentKey, count);

            List<BigInteger> previousCounts = mLevels.stream().
                                                      limit(i).
                                                      map(l -> l.getNodeCount()).
                                                      collect(Collectors.toList());
            BigInteger treeSizeOfPreviousLevel = BigInteger.ONE;
            if (previousCounts.size() > 0)
            {
                BigInteger aggregateVal = previousCounts.stream().
                        reduce((x, y) -> x.add(y)).get();
                treeSizeOfPreviousLevel.add(aggregateVal);
            }



            Level level = new Level();
            level.setNodeCount(count);
            level.setPrime(currentKey);
            level.setIndex(i);
            level.setNodesPerGroup(currentKey);
            level.setGroupCount(groupCount);
            level.setTreeSizeOfPreviousLevel(treeSizeOfPreviousLevel);

            this.mLevels.add(level);

        }

        BigInteger valueCount = BigInteger.ONE;
        for (Level level : this.mLevels)
        {
            level.setMinValue(valueCount.add(BigInteger.ONE));
            BigInteger maxVal = level.getNodeCount().add(valueCount);
            level.setMaxValue(maxVal);
            valueCount = maxVal;
        }
    }



}
