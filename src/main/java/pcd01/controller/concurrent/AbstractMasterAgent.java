package pcd01.controller.concurrent;

import gov.nasa.jpf.vm.Verify;
import pcd01.model.concurrent.AbstractTaskFactory;
import pcd01.model.SimulationState;
import pcd01.model.concurrent.TaskBag;
import pcd01.model.concurrent.TaskFactory;
import pcd01.utils.Chrono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public abstract class AbstractMasterAgent extends Thread {
    private final long maxSteps;
    private final List<WorkerAgent> workers;
    protected SimulationState state;
    protected int nWorker;
    protected TaskBag taskBag;
    protected TaskCompletionLatch taskLatch;
    protected AbstractTaskFactory taskFactory;

    public AbstractMasterAgent(SimulationState state, long maxSteps) {
        this.state = state;
        this.maxSteps = maxSteps;
        this.taskFactory = new TaskFactory();
        this.nWorker = Verify.getInt(1, Runtime.getRuntime().availableProcessors())+1;
        this.taskBag = new TaskBag();
        this.workers = new ArrayList<>(nWorker);
    }

    public void run() {

        Verify.beginAtomic();
        Chrono time = new Chrono();
        this.createWorkerAgents();
        time.start();
        Verify.endAtomic();
        while( state.getSteps() < maxSteps ){
            this.addComputeForcesTasksToBag();
            this.waitStepDone();

            this.addUpdatePositionTasksToBag();
            this.waitStepDone();
            state.setVt(state.getVt() + state.getDt());
            state.incrementSteps();
        }
        time.stop();
        for (WorkerAgent worker : this.workers) {
            worker.interrupt();
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

    private void createWorkerAgents() {
        IntStream.range(0, nWorker).forEach(a -> this.workers.add(new WorkerAgent(taskBag, taskLatch)));
        this.workers.forEach(Thread::start);
    }

    abstract void addComputeForcesTasksToBag();
    abstract void addUpdatePositionTasksToBag();
}
