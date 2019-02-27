package com.wangjun.concurrency.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

public class RecursiveActionDemo extends RecursiveAction {


    private static final int MAX = 20;
    private int start;
    private int end;

    public RecursiveActionDemo(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        if ((end - start) < MAX) {
            for (int i = start; i < end; i++) {
                System.out.println(Thread.currentThread().getName() + "i的值" + i);
            }
        } else {
            int middle = (start + end) / 2;
            RecursiveActionDemo left = new RecursiveActionDemo(start, middle);
            RecursiveActionDemo right = new RecursiveActionDemo(middle, end);
            left.fork();
            right.fork();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Runtime.getRuntime().availableProcessors());
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        // 提交可分解的PrintTask任务
        forkJoinPool.submit(new RecursiveActionDemo(0, 1000));

        //阻塞当前线程直到 ForkJoinPool 中所有的任务都执行结束
        forkJoinPool.awaitTermination(2, TimeUnit.SECONDS);

        // 关闭线程池
        forkJoinPool.shutdown();
    }
}