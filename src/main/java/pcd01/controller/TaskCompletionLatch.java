package pcd01.controller;

public class TaskCompletionLatch {

    private final int nTasks;
    private int nCompletionsNotified;

    TaskCompletionLatch(int nTasks){
        this.nTasks = nTasks;
        nCompletionsNotified = 0;
    }

    public synchronized void reset() {
        nCompletionsNotified = 0;
    }

    public synchronized void waitCompletion() throws InterruptedException {
        while (nCompletionsNotified < nTasks) {
            wait();
        }
    }

    public synchronized void notifyCompletion() {
        nCompletionsNotified++;
        notifyAll();
    }

}
