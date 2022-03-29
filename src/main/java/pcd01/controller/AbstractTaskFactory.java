package pcd01.controller;

import pcd01.model.Body;
import pcd01.model.SimulationState;

import java.util.ArrayList;
import java.util.List;

public interface AbstractTaskFactory {
    Task createComputeForcesTask(List<Body> body, SimulationState state);
    Task createUpdatePositionTask(List<Body> body, SimulationState state);
    Task createCheckCollisionTask(List<Body> body,  SimulationState state);
}
