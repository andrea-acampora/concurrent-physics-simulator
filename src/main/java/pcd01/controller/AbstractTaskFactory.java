package pcd01.controller;

import pcd01.model.Body;
import pcd01.model.SimulationState;

import java.util.ArrayList;
import java.util.List;

public interface AbstractTaskFactory {
    Task createComputeForcesTask(Body b, SimulationState state);
    Task createUpdatePositionTask(Body b, SimulationState state);
    Task createCheckCollisionTask(Body b,  SimulationState state);
}
