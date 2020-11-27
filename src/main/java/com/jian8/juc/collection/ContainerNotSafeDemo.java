package com.jian8.juc.collection;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 集合类不安全问题
 * ArrayList
 */
public class ContainerNotSafeDemo {
    public static void main(String[] args) {
        //    normer（）
        // notSafe();
        //vectorTest();
        //collectionsTest();
        copyOnWriteArrayListTest();
    }

    /**
     * 正常ArrayList
     */
    public static void normer() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        System.out.println(list);
    }

    /**
     * 1.故障现象
     * java.util.ConcurrentModificationException
     * 2.导致原因
     * 并发争抢导致，参考我们的花名册签名情况
     * 一个人正在写入，另外一个同学过来抢夺，导致数据不一致，并发修改异常
     * 3.解决方案
     * *  3.1 使用Vector
     * *  3.2 使用Collections辅助类 synchronizedList
     * *  3.3 CopyOnWriteArrayList
     * * 4.优化解决方案
     */
    public static void notSafe() {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(list);
            }, "Thread " + i).start();
        }
    }

    /**
     * 解决方案1：使用Vector
     */
    public static void vectorTest() {
        Vector<String> vector = new Vector<>();
        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                vector.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(Thread.currentThread().getName() + " : " + vector.toString());

            }, "Thread" + i).start();

        }
    }

    /**
     * 解决方案2
     * 使用Collections辅助类
     */
    public static void collectionsTest() {
        ArrayList<String> list = new ArrayList<>();
        List<String> synchronizedList = Collections.synchronizedList(list);

        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                synchronizedList.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(Thread.currentThread().getName() + " : " + synchronizedList.toString());

            }, "Thread" + i).start();

        }
    }

    /**
     * 优化解决方案3
     * CopyOnWriteArrayList
     * 写实复制，读写分离的思想
     */
    public static void copyOnWriteArrayListTest() {
        CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();

        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                copyOnWriteArrayList.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(Thread.currentThread().getName() + " : " + copyOnWriteArrayList.toString());

            }, "Thread" + i).start();
        }
    }
}
