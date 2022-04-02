package pcd01.model;

public interface AbstractTaskFactory {
    Task createComputeForcesTask(SimulationState state, Body... bodiesList);
    Task createUpdatePositionTask(SimulationState state, Body... bodiesList);
}
