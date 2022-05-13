package acherie.demos.core.concurrent;

import java.util.Comparator;
import java.util.PriorityQueue;


public class Solution {
    /**
     * 代码中的类名、方法名、参数名已经指定，请勿修改，直接返回方法规定的值即可
     *
     *
     * @param numbers int整型一维数组
     * @param k int整型
     * @return int整型一维数组
     */
    public int[] calSlidingWindowsArr (int[] numbers, int k) {
        // write code here
        if (k == 0) {
            return new int[]{};
        }
        if (numbers.length == 0) {
            return new int[]{};
        }
        int[] res = new int[numbers.length - k + 1];
        int left = 0;
        int right = k - 1;
        PriorityQueue<Integer> queue = new PriorityQueue<>(k, Comparator.reverseOrder());
        for (int i = 0; i < k; i++) {
            queue.add(numbers[i]);
        }
        res[0] = queue.peek();

        int i = 1;
        while (right < numbers.length - 1) {
            right++;
            queue.remove(numbers[left]);
            queue.add(numbers[right]);
            left++;
            res[left] = queue.peek();
        }

        return res;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] ints = solution.calSlidingWindowsArr(new int[]{1, 3, -1, -3, 5, 3, 6}, 3);
        for (int i : ints) {
            System.out.println(i);
        }
    }
}
