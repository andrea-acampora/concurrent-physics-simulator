package pcd01.controller;

public class StartSynch {
    private boolean started;

    public StartSynch(){
        started = false;
    }

    public synchronized void waitStart() {
        while (!started) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        started = false;
    }

    public synchronized void notifyStarted() {
        started = true;
        notifyAll();
    }

    public synchronized void reset(){
        started = false;
    }
}
