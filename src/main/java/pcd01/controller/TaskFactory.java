package pcd01.controller;

import pcd01.model.Body;
import pcd01.model.SimulationState;
import pcd01.utils.V2d;

public class TaskFactory implements AbstractTaskFactory{

    @Override
    public Task createComputeForcesTask(Body b, SimulationState state) {
        return () -> {
                /* compute total force on bodies */
                V2d totalForce = new V2d(0, 0);
                /* compute total repulsive force */
                for (int j = 0; j < state.getBodies().size(); j++) {
                    Body otherBody = state.getBodies().get(j);
                    if (!b.equals(otherBody)) {
                        try {
                            V2d forceByOtherBody = b.computeRepulsiveForceBy(otherBody);
                            totalForce.sum(forceByOtherBody);
                        } catch (Exception ignored) {}
                    }
                }
                /* add friction force */
                totalForce.sum(b.getCurrentFrictionForce());
                /* compute instant acceleration */
                V2d acc = new V2d(totalForce).scalarMul(1.0 / b.getMass());
                /* update velocity */
                b.updateVelocity(acc, state.getDt());
        };
    }

    @Override
    public Task createUpdatePositionTask(Body b, SimulationState state) {
        return () -> b.updatePos(state.getDt());
    }

    @Override
    public Task createCheckCollisionTask(Body b, SimulationState state) {
        return () ->  b.checkAndSolveBoundaryCollision(state.getBounds());
    }
}