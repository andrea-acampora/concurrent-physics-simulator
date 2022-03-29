package pcd01.controller;

import pcd01.model.Body;
import pcd01.model.SimulationState;

import java.util.ArrayList;

public interface AbstractTaskFactory {
    Task createComputeForcesTask(Body body, SimulationState state);
    Task createUpdatePositionTask(Body body, SimulationState state);
    Task createCheckCollisionTask(Body body,  SimulationState state);
}
