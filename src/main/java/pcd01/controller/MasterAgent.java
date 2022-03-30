package pcd01.controller;

import pcd01.model.SimulationState;
import pcd01.utils.Chrono;
import pcd01.view.View;

import java.util.stream.IntStream;

public class MasterAgent extends Thread {

    private final long maxSteps;
    private final View view;
    private final StartSynch synch;
    private final Flag stopFlag;
    private SimulationState state;
    private int nWorker;
    private TaskBag taskBag;
    private TaskCompletionLatch taskLatch;
    private final AbstractTaskFactory taskFactory;
    

    public MasterAgent(View view, SimulationState state, long maxSteps, Flag stopFlag, StartSynch synch) {
        this.state = state;
        this.taskFactory = new TaskFactory();
        this.maxSteps = maxSteps;
        this.view = view;
        this.nWorker = Runtime.getRuntime().availableProcessors() + 1;
        this.taskBag = new TaskBag();
        this.taskLatch = new TaskCompletionLatch(nWorker);
        this.stopFlag = stopFlag; 
        this.synch = synch; 
    }

    public void run() {

        Chrono time = new Chrono();
        this.createWorkerAgent();
        System.out.println("master waiting for start...");
        synch.waitStart();
        System.out.println("master started");
        time.start();
        System.out.println("flag = " + !stopFlag.isSet());
        while(state.getSteps() < maxSteps){
            this.addComputeForcesTasksToBag();

            this.waitStepDone();

            this.addUpdatePositionTasksToBag();

            this.waitStepDone();

            this.addCheckCollisionTasksToBag();

            this.waitStepDone();

            state.incrementSteps();
            state.setVt(state.getVt() + state.getDt());

            if(stopFlag.isSet()){
                synch.waitStart();
            }
            view.display(state);
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

    private void createWorkerAgent() {
        IntStream.range(0, nWorker).forEach(a -> new WorkerAgent(taskBag, taskLatch, stopFlag).start());
    }

    private void addComputeForcesTasksToBag() {
        state.getBodies().forEach( b -> taskBag.addNewTask(taskFactory.createComputeForcesTask(b, state)));
    }

    private void addUpdatePositionTasksToBag() {
        state.getBodies().forEach( b -> taskBag.addNewTask(taskFactory.createUpdatePositionTask(b, state)));
    }

    private void addCheckCollisionTasksToBag() {
        state.getBodies().forEach( b -> taskBag.addNewTask(taskFactory.createCheckCollisionTask(b, state)));
    }

    private void log(String msg){
        synchronized(System.out){
            System.out.println("[ master ] " + msg);
        }
    }

}
