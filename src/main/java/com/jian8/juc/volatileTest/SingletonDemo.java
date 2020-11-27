package com.jian8.juc.volatileTest;

public class SingletonDemo {
    //DCL（双端检锁）机制 加锁前和加锁后都进行一次判断
   private static volatile SingletonDemo singletonDemo = null;

   //用于synchronized锁对象
   private  static  Byte[] bytes = new Byte[0];

   private SingletonDemo(){
       System.out.println(Thread.currentThread().getName() + "\t 构造方法SingletonDemo()");
   }

   public static SingletonDemo getInstance(){
       if(singletonDemo == null){
           //synchronized (SingletonDemo.class)
           synchronized (bytes){
               if(singletonDemo == null){
                   singletonDemo = new SingletonDemo();
               }
           }
       }
       return singletonDemo;
   }

    public static void main(String[] args) {
        //构造方法只会被执行一次
//        System.out.println(getInstance() == getInstance());
//        System.out.println(getInstance() == getInstance());
//        System.out.println(getInstance() == getInstance());

        //构造方法会在一些情况下执行多次
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                SingletonDemo.getInstance();
            }, "Thread " + i).start();
        }
    }
}
