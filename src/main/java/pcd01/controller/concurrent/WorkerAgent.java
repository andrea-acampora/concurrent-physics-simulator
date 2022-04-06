package pcd01.controller.concurrent;

import pcd01.model.concurrent.Task;
import pcd01.model.concurrent.TaskBag;
import gov.nasa.jpf.vm.Verify;

public class WorkerAgent extends Thread {

    private final TaskBag bag;
    private final TaskCompletionLatch latch;

    public WorkerAgent(TaskBag bag, TaskCompletionLatch latch) {
        this.bag = bag;
        this.latch = latch;
    }

    @Override
    public void run() {
        while (true) {
            Verify.beginAtomic();
            Task task = bag.getATask();
            Verify.endAtomic();
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
