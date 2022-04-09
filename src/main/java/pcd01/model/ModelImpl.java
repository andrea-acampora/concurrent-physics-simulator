package pcd01.model;

import pcd01.model.concurrent.AbstractTaskFactory;
import pcd01.model.concurrent.TaskFactory;

public class ModelImpl implements Model {

    private final SimulationState state;
    private final AbstractTaskFactory taskFactory;

    public ModelImpl(final int numberOfBodies) {
        this.state = new SimulationState(numberOfBodies);
        this.taskFactory = new TaskFactory();
    }

    @Override
    public SimulationState getState() {
        return this.state;
    }

    @Override
    public AbstractTaskFactory getTaskFactory() {
        return this.taskFactory;
    }


}
