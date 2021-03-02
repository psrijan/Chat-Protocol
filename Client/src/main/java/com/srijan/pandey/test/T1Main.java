package com.srijan.pandey.test;

public class T1Main {

    public static void main(String[] args)  {
        T1 t1 = new T1();
        T2 t2 = new T2();

        Thread thread = new Thread();
        thread.start();

        Thread thread1 = new Thread();
        thread1.start();

    }
}
