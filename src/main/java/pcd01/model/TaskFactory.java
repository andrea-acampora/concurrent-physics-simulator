package pcd01.model;

import pcd01.utils.V2d;

import java.util.List;

public class TaskFactory implements AbstractTaskFactory {

    @Override
    public Task createComputeForcesTask(SimulationState state, Body... bodiesList) {
        return () -> {
            for (Body b : bodiesList) {
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
            }
        };
    }

    @Override
    public Task createUpdatePositionTask(SimulationState state, Body... bodiesList) {
        return () -> {
            /* compute bodies new pos */
            for (Body b : bodiesList) {
                b.updatePos(state.getDt());
                b.checkAndSolveBoundaryCollision(state.getBounds());
            }
        };
    }
}