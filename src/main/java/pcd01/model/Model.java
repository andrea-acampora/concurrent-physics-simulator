package pcd01.model;

import pcd01.model.concurrent.AbstractTaskFactory;

/**
 * The model of the application.
 *
 */
public interface Model {

    /**
     *
     * @return all the information about the current simulation.
     */
    SimulationState getState();

    /**
     *
     * @return the factory to create tasks.
     */
    AbstractTaskFactory getTaskFactory();
}
