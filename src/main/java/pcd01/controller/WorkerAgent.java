package pcd01.controller;

import pcd01.model.Task;
import pcd01.model.TaskBag;

public class WorkerAgent extends Thread {


    private final TaskBag bag;
    private final TaskCompletionLatch latch;
    private final Flag stopFlag;
    private final StartSynch synch;

    public WorkerAgent(TaskBag bag, TaskCompletionLatch latch, Flag stopFlag, StartSynch synch) {
        this.bag = bag;
        this.latch = latch;
        this.stopFlag = stopFlag;
        this.synch = synch; 
    }

    @Override
    public void run() {
        while (true) {
            Task task = bag.getATask();
            if(stopFlag.isSet()){
                System.out.println(stopFlag.isSet());
                log("wait");
                synch.waitStart();
                log("resumed");
                System.out.println(stopFlag.isSet());
            }
            task.computeTask();
            latch.notifyCompletion();
        }
    }

    private void log(String msg){
        synchronized(System.out){
            System.out.println("[ worker " + getName() + "] " + msg);
        }
    }
}
