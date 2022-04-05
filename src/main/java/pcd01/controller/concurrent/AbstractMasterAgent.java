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
    private final StartSynch synch;
    private final Flag stopFlag;
    protected SimulationState state;
    protected int nWorker;
    protected TaskBag taskBag;
    protected TaskCompletionLatch taskLatch;
    protected AbstractTaskFactory taskFactory;


    public AbstractMasterAgent(View view, SimulationState state, long maxSteps, Flag stopFlag, StartSynch synch) {
        this.state = state;
        this.maxSteps = maxSteps;
        this.view = view;
        this.taskFactory = new TaskFactory();
        this.nWorker = Runtime.getRuntime().availableProcessors() + 1;
        this.taskBag = new TaskBag();
        this.stopFlag = stopFlag;
        this.synch = synch;
    }

    public void run() {

        Chrono time = new Chrono();
        this.createWorkerAgents();
        if(Main.VIEW_ENABLED){
            synch.waitStart();
        }
        time.start();
        while(state.getSteps() < maxSteps){
            this.addComputeForcesTasksToBag();
            this.waitStepDone();

            this.addUpdatePositionTasksToBag();
            this.waitStepDone();

            state.setVt(state.getVt() + state.getDt());

            if(stopFlag.isSet()){
                synch.waitStart();
            }
            log("finished step " + state.getSteps() + " - pos of ball 0" + state.getBodies().get(0).getPos());

            if(Main.VIEW_ENABLED){
                view.display(state);
            }
            state.incrementSteps();
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
        IntStream.range(0, nWorker).forEach(a -> new WorkerAgent(taskBag, taskLatch, stopFlag, synch).start());
    }

    abstract void addComputeForcesTasksToBag();
    abstract void addUpdatePositionTasksToBag();

    private void log(String msg){
        synchronized(System.out){
            System.out.println("[ master ] " + msg);
        }
    }
}
