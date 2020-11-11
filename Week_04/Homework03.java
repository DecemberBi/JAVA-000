package java0.conc0303;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 * <p>
 * 一个简单的代码参考：
 */
public class Homework03 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int result;

        long start = System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法
        // 方法1. new Thread，包装FutureTask实现Callable
        FutureTask<Integer> task = new FutureTask<>(Homework03::sum);
        new Thread(task).start();
        result = task.get();
        System.out.println("方法1结果：" + result);

        // 方法2. CompleteableFuture使用内置线程池
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(Homework03::sum);
        result = future.get();
        System.out.println("方法2结果：" + result);

        // 方法3. 线程池提交
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<Integer> submit = executor.submit(Homework03::sum);
        result = submit.get();
        System.out.println("方法3结果：" + result);

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

        // 然后退出main线程
        executor.shutdown();
    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if (a < 2)
            return 1;
        return fibo(a - 1) + fibo(a - 2);
    }
}
