package pcd01.controller;

import pcd01.model.Body;
import pcd01.model.SimulationState;
import pcd01.view.SimulationView;
import pcd01.view.View;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class MasterAgent extends Thread {

    private final long maxSteps;
    private final View view;
    private SimulationState state;
    private int nWorker;
    TaskBag taskBag;
    TaskCompletionLatch taskLatch;
    AbstractTaskFactory taskFactory;

    public MasterAgent(View view, SimulationState state, long maxSteps) {
        this.state = state;
        this.taskFactory = new TaskFactory();
        this.maxSteps = maxSteps;
        this.view = view;
    }

    public void run() {
        this.nWorker = Runtime.getRuntime().availableProcessors() + 1;
        this.taskBag = new TaskBag();
        taskLatch = new TaskCompletionLatch(nWorker);
        this.createWorkerAgent();

        while(state.getSteps() < maxSteps){
            this.addComputeForcesTasksToBag();
            this.waitStepDone();
            this.addUpdatePositionTasksToBag();
            this.waitStepDone();
            this.addCheckCollisionTasksToBag();
            this.waitStepDone();
            state.incrementSteps();
            state.setVt(state.getVt() + state.getDt());
            view.display(state);
        }
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
        IntStream.rangeClosed(0, nWorker).forEach(a -> new WorkerAgent(taskBag, taskLatch).start());
    }

    private void addComputeForcesTasksToBag() {
        state.getBodies().forEach(b -> taskBag.addNewTask(taskFactory.createComputeForcesTask(b, state)));
    }

    private void addUpdatePositionTasksToBag() {
        state.getBodies().forEach(b -> taskBag.addNewTask(taskFactory.createUpdatePositionTask(b, state)));
    }

    private void addCheckCollisionTasksToBag() {
        state.getBodies().forEach(b -> taskBag.addNewTask(taskFactory.createCheckCollisionTask(b, state)));
    }
    //to do elaborations
}

