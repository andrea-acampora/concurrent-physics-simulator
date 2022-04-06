package pcd01.controller.concurrent;

import pcd01.model.concurrent.Task;
import pcd01.model.concurrent.TaskBag;

public class WorkerAgent extends Thread {

    private final TaskBag bag;
    private final TaskCompletionLatch latch;
    private final Flag stopFlag;
    private final StartSynch startSynch;

    public WorkerAgent(TaskBag bag, TaskCompletionLatch latch, Flag stopFlag, StartSynch startSynch) {
        this.bag = bag;
        this.latch = latch;
        this.stopFlag = stopFlag;
        this.startSynch = startSynch;
    }

    @Override
    public void run() {
        while (true) {
            if (stopFlag.isSet()) {
                startSynch.waitStart();
            }
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
