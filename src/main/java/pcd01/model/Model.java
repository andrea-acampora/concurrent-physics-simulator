package pcd01.model;

import pcd01.model.concurrent.AbstractTaskFactory;

public interface Model {

    SimulationState getState();

    AbstractTaskFactory getTaskFactory();
}
