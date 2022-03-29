package pcd01.controller;

import pcd01.model.Body;

import java.util.ArrayList;

public class WorkerAgent extends Thread{


    private final TaskBag bag;
    private final TaskCompletionLatch latch;

    public WorkerAgent(TaskBag bag, TaskCompletionLatch latch) {
        this.bag = bag;
        this.latch = latch;
    }

    @Override
    public void run() {
        while(true){
            Task task = bag.getATask();
            task.computeTask();
            latch.notifyCompletion();
        }
    }
}
