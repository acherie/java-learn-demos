package acherie.demos.core.concurrent;

import org.springframework.util.StopWatch;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

public class NumberSumConcurrent {

    public static void main(String[] args) {

//        int max = 1000;
        int max = Integer.MAX_VALUE - 1;

        StopWatch longAdder = new StopWatch("LongAdder");
        // method 1
        longAdder.start("m1");
        long sum1 = LongStream.rangeClosed(1, max)
                .parallel()
                .reduce(0, Long::sum);
        longAdder.stop();
        System.out.println("sum1 = " + sum1);

        // method 2
        longAdder.start("m2");
        long sum2 = LongStream.rangeClosed(1, max)
                .parallel()
                .sum();
        longAdder.stop();
        System.out.println("sum2 = " + sum2);

        longAdder.start("m2");
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Long sum3 = forkJoinPool.invoke(new SumTask(1, max + 1));
        longAdder.stop();
        System.out.println("sum3 = " + sum3);

        System.out.println(longAdder.prettyPrint());
    }

    public static class SumTask extends RecursiveTask<Long> {

        private final static int MAX = 20;
        private final long start;
        private final long end;

        public SumTask(long start, long end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {

            if (end - start < MAX) {
                long sum = 0;
                for (long i = start; i < end; i++) {
                    sum += i;
                }
                return sum;
            } else {
                long mid = start + (end - start) / 2;
                SumTask t1 = new SumTask(start, mid);
                SumTask t2 = new SumTask(mid, end);
                t1.fork();
                t2.fork();
                return t1.join() + t2.join();
            }
        }
    }
}
