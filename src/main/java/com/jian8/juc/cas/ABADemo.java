package com.jian8.juc.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * ABA问题解决
 * AtomicStampedReference
 */
public class ABADemo {
    private static AtomicReference<Integer> atomicReference = new AtomicReference(100);

    private static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(100, 1);

    public static void main(String[] args) {
        System.out.println("-------ABA问题的产生-------");
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " : " + atomicReference.compareAndSet(100, 101) + "\t " + atomicReference.get());
            System.out.println(Thread.currentThread().getName() + " : " + atomicReference.compareAndSet(101, 100) + "\t " + atomicReference.get());
        }, "ThreadA").start();

        new Thread(() -> {
            try {
                //让线程A先处理完成 保证线程A完成一次ABA操作
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " : " + atomicReference.compareAndSet(100, 2019) + "\t " + atomicReference.get());
        }, "ThreadB").start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("-------ABA问题解决-------");

        new Thread(()->{
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t 第一次版本号 : " + atomicStampedReference.getStamp());

            try {
                //让线程D先获取到版本号
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(atomicStampedReference.compareAndSet(100, 101,atomicStampedReference.getStamp(),atomicStampedReference.getStamp()+1) + "\t 第二次版本号 : " + atomicStampedReference.getStamp());

            System.out.println(atomicStampedReference.compareAndSet(101, 100,atomicStampedReference.getStamp(),atomicStampedReference.getStamp()+1) + "\t 第三次版本号 : " + atomicStampedReference.getStamp());
        },"ThreadC").start();

        new Thread(()->{
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t 第一次版本号 : " + atomicStampedReference.getStamp());

            try {
                //让线程C先走完比较交换
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            boolean result = atomicStampedReference.compareAndSet(100, 2019, stamp, stamp + 1);

            System.out.println(Thread.currentThread().getName() + "\t修改是否成功" + result + "\t当前最新实际版本号：" + atomicStampedReference.getStamp());
            System.out.println(Thread.currentThread().getName() + "\t当前最新实际值：" + atomicStampedReference.getReference());
        },"ThreadD").start();

    }


}
