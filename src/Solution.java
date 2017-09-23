import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Solution {

    List<Integer> plan(List<BigInteger> weights) {
        List<Integer> resultSet = new ArrayList<>();
        if (weights.isEmpty()) {
            return resultSet;
        }
        if (weights.size() == 1) {
            resultSet.add(1);
            return resultSet;
        }
        if (weights.size() == 2) {
            if (weights.get(0).compareTo(weights.get(1)) > 0) {
                resultSet.add(1);
            }
            else {
                resultSet.add(2);
            }
            return resultSet;
        }

        BigInteger[] subProblems;
        subProblems = new BigInteger[weights.size()];
        subProblems[0] = weights.get(0);
        subProblems[1] = weights.get(0).compareTo(weights.get(1)) > 0 ? weights.get(0) : weights.get(1);
        for (int i = 2; i < subProblems.length; i++) {
            BigInteger excluded = subProblems[i - 1];
            BigInteger included = subProblems[i - 2].add(weights.get(i));
            if (excluded.compareTo(included) > 0) {
                subProblems[i] = excluded;
            }
            else {
                subProblems[i] = included;
            }
        }
        for (int i = subProblems.length - 1; i >= 0; ) {
            BigInteger maxWeight = subProblems[i];
            if (i == 1) {
                if (subProblems[0].compareTo(subProblems[1]) < 0) {
                    resultSet.add(2);
                }
                else {
                    resultSet.add(1);
                }
                break;
            }
            else if (i == 0) {
                resultSet.add(1);
                break;
            }
            else {
                BigInteger excluded = subProblems[i - 1];
                BigInteger included = subProblems[i - 2].add(weights.get(i));
                if (included.compareTo(maxWeight) == 0) {
                    resultSet.add(i + 1);
                    i -= 2;
                } else {
                    i -= 1;
                }
            }
        }

        return resultSet;
    }

    @Test
    public void tc1() {
        Solution solution = new Solution();
        List<Integer> mwi = solution.plan(Arrays.asList(BigInteger.valueOf(1l), BigInteger.valueOf(4l), BigInteger.valueOf(5l), BigInteger.valueOf(4l)));
        Assert.assertEquals(2, mwi.size());
        Assert.assertEquals(4, mwi.get(0).intValue());
        Assert.assertEquals(2, mwi.get(1).intValue());
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(Paths.get("mwis.txt"));
        int nodeNumber = scanner.nextInt();
        List<BigInteger> weights = new ArrayList<>();
        for (int i = 0; i < nodeNumber; i++) {
            BigInteger weight = scanner.nextBigInteger();
            weights.add(weight);
        }
        List<Integer> plan = new Solution().plan(weights);
        List<Integer> curious = Arrays.asList(1, 2, 3, 4, 17, 117, 517, 997);
        StringBuilder str = new StringBuilder();
        for (Integer node : curious) {
            int i = Collections.binarySearch(plan, node, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    if (o1 > o2) {
                        return -1;
                    }
                    if (o1 < o2) {
                        return 1;
                    }
                    return 0;
                }
            });
            if (i >= 0) {
                str.append('1');
            }
            else {
                str.append('0');
            }
        }
        System.out.println(str);
    }
}
