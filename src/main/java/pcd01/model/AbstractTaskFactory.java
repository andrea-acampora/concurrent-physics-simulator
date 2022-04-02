package pcd01.model;

public interface AbstractTaskFactory {
    Task createComputeForcesTask(Body b, SimulationState state);
    Task createUpdatePositionTask(Body b, SimulationState state);
    Task createCheckCollisionTask(Body b,  SimulationState state);
}
