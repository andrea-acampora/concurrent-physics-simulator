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
        try {
            while ( true ) {
                Task task = bag.getATask();
                Verify.beginAtomic();
                task.computeTask();
                Verify.endAtomic();
                latch.notifyCompletion();
            }
        } catch (InterruptedException ignored) {}
       // log("finished");
    }

    private void log(String msg){
        synchronized(System.out){
            System.out.println("[ worker " + getName() + "] " + msg);
        }
    }
}
