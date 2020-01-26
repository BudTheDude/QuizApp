package com.example.se.fightingthreads;


import java.util.concurrent.BlockingQueue;

public class ThreadB extends Thread {
    protected BlockingQueue blockingQueue;
    private int mb = 1;


    public ThreadB(BlockingQueue blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        try {
            while (!interrupted() && mb <= 10) {
                blockingQueue.take();
                System.out.println("ThreadB reached" + mb);
                Thread.sleep(200);
                blockingQueue.put(mb);
                mb++;
            }
        } catch (InterruptedException e) {
        }
    }

    public int cancel() {
        interrupt();
        return mb;
    }
}
