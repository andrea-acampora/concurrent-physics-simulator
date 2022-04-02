package pcd01.controller;

import pcd01.model.Task;
import pcd01.model.TaskBag;

public class WorkerAgent extends Thread {


    private final TaskBag bag;
    private final TaskCompletionLatch latch;
    private final Flag stopFlag;

    public WorkerAgent(TaskBag bag, TaskCompletionLatch latch, Flag stopFlag) {
        this.bag = bag;
        this.latch = latch;
        this.stopFlag = stopFlag;
    }

    @Override
    public void run() {
        while (true) {
            Task task = bag.getATask();
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
