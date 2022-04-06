package pcd01.controller.concurrent;

import pcd01.application.Main;
import pcd01.model.concurrent.AbstractTaskFactory;
import pcd01.model.SimulationState;
import pcd01.model.concurrent.TaskBag;
import pcd01.model.concurrent.TaskFactory;
import pcd01.utils.Chrono;
import pcd01.view.View;

import java.util.stream.IntStream;

public abstract class AbstractMasterAgent extends Thread{
    private final long maxSteps;
    private final View view;
    private final Flag stopFlag;
    private final StartSynch startSynch;
    protected SimulationState state;
    protected int nWorker;
    protected TaskBag taskBag;
    protected TaskCompletionLatch taskLatch;
    protected AbstractTaskFactory taskFactory;
    private static final double FPS = 60;


    public AbstractMasterAgent(View view, SimulationState state, long maxSteps, Flag stopFlag, StartSynch startSynch) {
        this.state = state;
        this.maxSteps = maxSteps;
        this.view = view;
        this.taskFactory = new TaskFactory();
        this.nWorker = Runtime.getRuntime().availableProcessors() + 1;
        this.taskBag = new TaskBag();
        this.stopFlag = stopFlag;
        this.startSynch = startSynch;
    }

    public void run() {

        Chrono time = new Chrono();
        startSynch.waitStart();
        this.createWorkerAgents();
        time.start();
        while( state.getSteps() < maxSteps ){
            long initialTime = System.currentTimeMillis();
            if(stopFlag.isSet()){
                startSynch.waitStart();
            }

            this.addComputeForcesTasksToBag();
            this.waitStepDone();

            this.addUpdatePositionTasksToBag();
            this.waitStepDone();

            state.setVt(state.getVt() + state.getDt());
            view.display(state);
            state.incrementSteps();
            double elapsed = System.currentTimeMillis() - initialTime;
            if (elapsed < ((1 / FPS)*1000)) {
                try {
                    Thread.sleep((long) (((1 / FPS)*1000) - elapsed));
                } catch(Exception ignored){}
            }
        }
        time.stop();
        System.out.println("Time elapsed: " + time.getTime() + " ms.");
        System.exit(0);
    }

    private void waitStepDone() {
        try {
            taskLatch.waitCompletion();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.taskLatch.reset();
        this.taskBag.clear();
    }

    private void createWorkerAgents() {
        IntStream.range(0, nWorker).forEach(a -> new WorkerAgent(taskBag, taskLatch, stopFlag, startSynch).start());
    }

    abstract void addComputeForcesTasksToBag();
    abstract void addUpdatePositionTasksToBag();

    private void log(String msg){
        synchronized(System.out){
            System.out.println("[ master ] " + msg);
        }
    }
}
