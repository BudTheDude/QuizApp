package com.example.se.fightingthreads;

import java.util.concurrent.BlockingQueue;


public class ThreadA extends Thread {


    protected BlockingQueue blockingQueue;
    private int ma = 1;


    public ThreadA(BlockingQueue blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        try {
            while (!interrupted() && ma <= 10) {

                blockingQueue.put(ma);
                System.out.println("ThreadA reached" + ma);
                Thread.sleep(200);
                ma++;
                blockingQueue.take();
            }
        } catch (InterruptedException e) {
        }

    }

    public int cancel() {
        interrupt();
        return ma;
    }
}
