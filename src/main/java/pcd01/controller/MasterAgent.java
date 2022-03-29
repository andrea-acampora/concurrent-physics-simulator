package pcd01.controller;

import com.google.common.collect.Lists;
import pcd01.model.Body;
import pcd01.model.SimulationState;
import pcd01.utils.Chrono;
import pcd01.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class MasterAgent extends Thread {

    private final long maxSteps;
    private final View view;
    private SimulationState state;
    private int nWorker;
    private TaskBag taskBag;
    private TaskCompletionLatch taskLatch;
    private final AbstractTaskFactory taskFactory;
    private List<List<Body>> bodiesSplit;

    public MasterAgent(View view, SimulationState state, long maxSteps) {
        this.state = state;
        this.taskFactory = new TaskFactory();
        this.maxSteps = maxSteps;
        this.view = view;
        this.nWorker = Runtime.getRuntime().availableProcessors() + 1;
        this.bodiesSplit = Lists.partition(state.getBodies(),  state.getBodies().size() / nWorker +1);

        this.bodiesSplit.forEach((a) -> System.out.println("size: " +a.size()));
        this.taskBag = new TaskBag();
        taskLatch = new TaskCompletionLatch(nWorker);
    }

    public void run() {

        Chrono chrono = new Chrono();
        chrono.start();
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
        IntStream.rangeClosed(0, nWorker).forEach(a -> new WorkerAgent(taskBag, taskLatch).start());
    }

    private void addComputeForcesTasksToBag() {
        IntStream.range(0, nWorker).forEach((i) -> taskBag.addNewTask(taskFactory.createComputeForcesTask(bodiesSplit.get(i), state)));
    }

    private void addUpdatePositionTasksToBag() {
        IntStream.range(0, nWorker).forEach((i) -> taskBag.addNewTask(taskFactory.createUpdatePositionTask(bodiesSplit.get(i), state)));
    }

    private void addCheckCollisionTasksToBag() {
        IntStream.range(0, nWorker).forEach((i) -> taskBag.addNewTask(taskFactory.createCheckCollisionTask(bodiesSplit.get(i), state)));
    }

    private void log(String msg){
        synchronized(System.out){
            System.out.println("[ master ] " + msg);
        }
    }

}
