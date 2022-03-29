package pcd01.controller;

import pcd01.model.Body;
import pcd01.model.SimulationState;
import pcd01.view.SimulationView;
import pcd01.view.View;

import java.util.ArrayList;


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
        for (int i = 0; i < nWorker; i++) {
            new WorkerAgent(taskBag, taskLatch).start();
        }
    }

    private void addComputeForcesTasksToBag() {
        for (int i = 0; i < state.getBodies().size(); i++) {
            Body body = state.getBodies().get(i);
            taskBag.addNewTask(taskFactory.createComputeForcesTask(body, state));
        }
    }

    private void addUpdatePositionTasksToBag() {
        for (int i = 0; i < state.getBodies().size(); i++) {
            Body body = state.getBodies().get(i);
            taskBag.addNewTask(taskFactory.createUpdatePositionTask(body, state));
        }
    }

    private void addCheckCollisionTasksToBag() {
        for (int i = 0; i < state.getBodies().size(); i++) {
            Body body = state.getBodies().get(i);
            taskBag.addNewTask(taskFactory.createCheckCollisionTask(body, state));
        }
    }


    //to do elaborations
}

