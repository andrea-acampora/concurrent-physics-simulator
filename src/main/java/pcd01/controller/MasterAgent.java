package pcd01.controller;

import pcd01.model.Body;
import pcd01.model.SimulationState;
import pcd01.utils.Chrono;
import pcd01.view.SimulationView;
import pcd01.view.View;

import java.util.ArrayList;
import java.util.List;


public class MasterAgent extends Thread {

    private final long maxSteps;
    private final View view;
    private SimulationState state;
    private int nWorker;
    private TaskBag taskBag;
    private TaskCompletionLatch taskLatch;
    private final AbstractTaskFactory taskFactory;

    public MasterAgent(View view, SimulationState state, long maxSteps) {
        this.state = state;
        this.taskFactory = new TaskFactory();
        this.maxSteps = maxSteps;
        this.view = view;
    }

    public void run() {

        log("started");

        Chrono chrono = new Chrono();
        chrono.start();

        this.nWorker = Runtime.getRuntime().availableProcessors() + 1;
        this.taskBag = new TaskBag();
        taskLatch = new TaskCompletionLatch(nWorker);

        log("Create " + nWorker + " worker agents");
        this.createWorkerAgent();

        while(state.getSteps() < maxSteps){
            log("Add computeForcesTasks to the bag");
            this.addComputeForcesTasksToBag();

            log("Waiting completion of first task of this step");
            this.waitStepDone();
            log("Waking up.. first task finished!");

            log("Add updatePositionTasks to the bag");
            this.addUpdatePositionTasksToBag();

            log("Waiting completion of second task of this step");
            this.waitStepDone();
            log("Waking up.. second task finished!");

            log("Add checkCollisionTasks to the bag");
            this.addCheckCollisionTasksToBag();

            log("Waiting completion of third task of this step");
            this.waitStepDone();
            log("Waking up.. third task finished!");

            log("Incrementing step of simulation and display view");
            state.incrementSteps();
            state.setVt(state.getVt() + state.getDt());
            view.display(state);
        }

        chrono.stop();
        System.out.println("Time elapsed: " + chrono.getTime() / 1000+ " seconds.");
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

    private void log(String msg){
        synchronized(System.out){
            System.out.println("[ master ] " + msg);
        }
    }

}
