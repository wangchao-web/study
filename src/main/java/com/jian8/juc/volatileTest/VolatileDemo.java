package com.jian8.juc.volatileTest;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 1验证volatile的可见性
 * 1.1 如果int num = 0，number变量没有添加volatile关键字修饰
 * 1.2 添加了volatile，可以解决可见性
 * <p>
 * 2.验证volatile不保证原子性
 * 2.1 原子性指的是什么
 * 不可分割、完整性，即某个线程正在做某个具体业务时，中间不可以被加塞或者被分割，需要整体完整，要么同时成功，要么同时失败
 * 2.2 如何解决原子性
 * 2.2.1 方法加synchronized
 * 2.2.2 Atomic
 */
public class VolatileDemo {

    public static void main(String[] args) {
        visibility();
        //visibilityByVolatile();//验证volatile的可见性
        //atomicByVolatile();//验证volatile不保证原子性
    }

    /**
     * volatile可以保证可见性，及时通知其他线程，主物理内存的值已经被修改
     */
    public static void visibilityByVolatile() {
        MyData myData = new MyData();

        //第一个线程
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t come in");
            try {
                //线程暂停3s
                TimeUnit.SECONDS.sleep(3);
                myData.num = 60;
                System.out.println(Thread.currentThread().getName() + "\t update value:" + myData.num);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }, "thread1").start();
        //第二个线程是main线程
        while (myData.num == 0) {
            //System.out.println(Thread.currentThread().getName() + "\t mission is over, num value is " );
            //如果myData的num一直为零，main线程一直在这里循环
        }

        System.out.println(Thread.currentThread().getName() + "\t end is over, num value is " + myData.num);
    }

    /**
     * volatile可以保证可见性，及时通知其他线程，主物理内存的值已经被修改
     */
    public static void visibility() {
        MyData myData = new MyData();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t myData value " + myData.num);
            try {
                Thread.sleep(1000);
                myData.addToSixty();
                System.out.println(Thread.currentThread().getName() + "\t myData value " + myData.num);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Thread1").start();

        new Thread(() -> {
            while (myData.num == 0) {
            }
            System.out.println(Thread.currentThread().getName() + "\t end is over, num value is " + myData.num);
        }, "Thread2").start();


    }

    /**
     * volatile不保证原子性
     * 以及使用Atomic保证原子性
     */
    public static void atomicByVolatile() {
        MyData myData = new MyData();
        //创建20个线程,里面自增100
        for (int i = 1; i <=20; i++) {
            new Thread(() -> {

                for (int j = 1; j <= 100; j++) {
                    myData.addSelf();
                    myData.atomicAddSelf();
                }

            }, "Thread "+ i).start();
        }

        //等待上面的线程都计算完成后，再用main线程取得最终结果值
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //判断线程数是否大于二
        while(Thread.activeCount() > 2){
            Thread.yield();
        }

        System.out.println(Thread.currentThread().getName()+"\t finally num value is "+myData.num);
        System.out.println(Thread.currentThread().getName()+"\t finally atomicnum value is "+myData.atomicInteger);
    }
}

/**
 * 资源类
 */
class MyData {
    int num = 0;

    //volatile int num = 0;

    public void addToSixty() {
        this.num = 60;
    }

    public void addSelf() {
        num++;
    }
    AtomicInteger atomicInteger = new AtomicInteger();

    public void atomicAddSelf() {
        atomicInteger.getAndIncrement();
    }
}

class ResortSeq {
    int a = 0;
    boolean flag = false;

    public void method01() {
        a = 1;
        flag = true;
    }

    public void method02() {
        if (flag) {
            a = a + 5;
            System.out.println("\"a\" value is " + a);
        }
    }
}