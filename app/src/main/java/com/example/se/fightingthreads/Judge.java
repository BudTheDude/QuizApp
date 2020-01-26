package com.example.se.fightingthreads;



import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class Judge {

    private BlockingQueue blockingQueue = new SynchronousQueue();

    public boolean start() {
        ThreadA threadA = new ThreadA(blockingQueue);
        ThreadB threadB = new ThreadB(blockingQueue);
        try {
            threadA.start();
            threadB.start();

            Random random = new Random();
            Thread.sleep(random.nextInt(2000));
            int ma =threadA.cancel();
            int mb =threadB.cancel();

            if(random.nextInt(9) + 1 < ma) {

                System.out.println("The elders have spoken, the exchange is fair");
                return true;
            }else{
                System.out.println("The elders have spoken, the exchange is NOT fair");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

}
