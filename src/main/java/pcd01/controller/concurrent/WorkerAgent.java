package pcd01.controller.concurrent;

import pcd01.model.concurrent.Task;
import pcd01.model.concurrent.TaskBag;

public class WorkerAgent extends Thread {

    private final TaskBag bag;
    private final TaskCompletionLatch latch;

    public WorkerAgent(TaskBag bag, TaskCompletionLatch latch) {
        this.bag = bag;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            while ( true ) {
                Task task = bag.getATask();
                task.computeTask();
                latch.notifyCompletion();
            }
        } catch (InterruptedException ignored) {}
    }
}
