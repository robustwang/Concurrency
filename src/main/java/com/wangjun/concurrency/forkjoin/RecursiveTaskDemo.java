package com.wangjun.concurrency.forkjoin;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class RecursiveTaskDemo extends RecursiveTask<Integer> {

    private static final int MAX = 70;
    private int arr[];
    private int start;
    private int end;


    public RecursiveTaskDemo(int[] arr, int start, int end) {
        this.arr = arr;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;
        if ((end - start) < MAX) {
            for (int i = start; i < end; i++) {
                sum += arr[i];
            }
            return sum;
        } else {
            System.err.println("=====任务分解======");
            int middle = (start + end) / 2;
            RecursiveTaskDemo left = new RecursiveTaskDemo(arr, start, middle);
            RecursiveTaskDemo right = new RecursiveTaskDemo(arr, middle, end);
            left.fork();
            right.fork();
            return left.join() + right.join();
        }
    }

    public static void main(String[] args) {
        int arr[] = new int[1000];
        Random random = new Random();
        int total = 0;
        for (int i = 0; i < arr.length; i++) {
            int temp = random.nextInt(100);
            total += (arr[i] = temp);
        }
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        // 提交可分解的PrintTask任务
//        Future<Integer> future = forkJoinPool.submit(new RecursiveTaskDemo(arr, 0, arr.length));
//        System.out.println("计算出来的总和="+future.get());

        Integer result = forkJoinPool.invoke(new RecursiveTaskDemo(arr, 0, arr.length));
        System.out.println(result);
        // 关闭线程池
        forkJoinPool.shutdown();
    }
}