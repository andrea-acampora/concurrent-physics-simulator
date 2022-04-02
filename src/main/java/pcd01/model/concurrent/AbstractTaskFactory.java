package pcd01.model.concurrent;

import pcd01.model.Body;
import pcd01.model.SimulationState;

import java.util.List;

public interface AbstractTaskFactory {
    Task createComputeForcesTask(SimulationState state, List<Body> bodiesList);
    Task createUpdatePositionTask(SimulationState state, List<Body> bodiesList);
}
