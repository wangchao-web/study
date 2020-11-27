package com.jian8.juc.cas;

import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicRefrenceDemo {
    public static void main(String[] args) {
        User zs = new User("zs", 18);
        User l4 = new User("l4", 20);
        User w5 = new User("w5", 25);
        AtomicReference<User> userAtomicReference = new AtomicReference<>();
        userAtomicReference.set(zs);
        //userAtomicReference.set(l4);
        System.out.println(userAtomicReference.compareAndSet(zs,l4) + " : " + userAtomicReference.get().toString());
        //System.out.println(userAtomicReference.compareAndSet(l4,zs) + " : " + userAtomicReference.get().toString());
        System.out.println(userAtomicReference.compareAndSet(zs,l4) + " : " + userAtomicReference.get().toString());

        //先get到再set值进去
        System.out.println(userAtomicReference.getAndSet(w5) + " : " + userAtomicReference.get().toString());


    }
}

@Getter
@ToString
class User {
    public User(String userName, int age) {
        this.userName = userName;
        this.age = age;
    }

    String userName;
    int age;
}