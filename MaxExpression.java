package com.zehua.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MaxExpression {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        while (scan.hasNextLine()) {
            String str = scan.nextLine();
            System.out.println(exec(str));
        }
    }

    public static int exec(String str) {
        return maxExpression(str);
    }

    // 表达式的最大值
    private static int maxExpression(String str) {
        String[] strs = str.split(" ");

        List<Object> list = getNumsAndExps(strs);
        int[] nums = (int[]) list.get(0);
        char[] exps = (char[]) list.get(1);
        int numsSize = nums.length;
        int[][] dp = new int[numsSize][numsSize];

        // 这一层循环是核心代码
        // 是根据解题思路翻译成代码
        for (int i = 0; i < numsSize; i++) {
            for (int j = 0; j < numsSize; j++) {
                if (j + i >= numsSize) {
                    break;
                }
                if (i == 0) {
                    // 如果在该数本身加括号，得到的最大值就是该数本身
                    dp[j][j + i] = nums[j];
                } else if (i == 1) {
                    // 如果在相邻两个数之间加括号，最大值就等于这两个数与对应的操作符运算
                    dp[j][j + i] = calculate(nums[j], nums[j + i], exps[j]);
                } else {
                    // 核心的递推公式在这里
                    dp[j][j + i] = maxExpressionIJ(exps, dp, j, j + i);
                }
            }
        }

        // 返回从第一个数到最后一个数表达式的最大值
        return dp[0][numsSize - 1];
    }

    // 计算两个数的结果
    private static int calculate(int a, int b, char exp) {
        if (exp == '*') {
            return a * b;
        } else if (exp == '+') {
            return a + b;
        }

        return 0;
    }

    // 计算dp[i][j]
    private static int maxExpressionIJ(char[] exps, int[][] dp, int i, int j) {
        int max = Integer.MIN_VALUE;
        for (int x = 1; x <= (j - i + 1) / 2; x++) {
            int temp1 = calculate(dp[i][i + x - 1], dp[i + x][j], exps[i + x - 1]);
            int temp2 = calculate(dp[i][j - x], dp[j - x + 1][j], exps[j - x]);
            int tempMax = max(temp1, temp2);
            if (tempMax > max) {
                max = tempMax;
            }
        }

        return max;
    }

    // 源字符串数组转换成操作数数组以及运算符数组
    private static List<Object> getNumsAndExps(String[] array) {
        int len = array.length;
        int numsSize = 0;
        int expsSize = 0;
        int[] nums = new int[len];
        char[] exps = new char[len];
        List<Object> result = new ArrayList<>();

        boolean minusFlag = false;
        for (int i = 0; i < len; i++) {
            boolean flag = isMultiOrAdd(array[i]);

            if (flag) {
                exps[expsSize++] = array[i].charAt(0);
            } else {
                boolean minusFlag1 = isMinus(array[i]);
                if (minusFlag1) {
                    minusFlag = minusFlag1;
                    continue;
                }
                int charI = Integer.parseInt(array[i]);
                if (minusFlag) {
                    charI = -charI;
                    minusFlag = false;
                }
                nums[numsSize++] = charI;
            }
        }

        int[] numsTmp = new int[numsSize];
        char[] expsTmp = new char[expsSize];
        for (int i = 0; i < numsSize; i++) {
            numsTmp[i] = nums[i];
        }

        for (int i = 0; i < expsSize; i++) {
            expsTmp[i] = exps[i];
        }

        result.add(numsTmp);
        result.add(expsTmp);

        return result;
    }

    // 判断是否是加好或者乘号
    private static boolean isMultiOrAdd(String str) {
        if (str.length() != 1) {
            return false;
        }

        char ch = str.charAt(0);
        if (ch == '*' || ch == '+') {
            return true;
        }

        return false;
    }

    // 判断是否为减号
    private static boolean isMinus(String str) {
        if (str.length() != 1) {
            return false;
        }

        char ch = str.charAt(0);
        if (ch == '-') {
            return true;
        }

        return false;
    }

    // 比较两个数的大小
    private static int max(int a, int b) {
        if (a >= b) {
            return a;
        }

        return b;
    }

}
