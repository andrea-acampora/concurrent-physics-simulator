package pcd01.model;

import pcd01.utils.V2d;

import java.util.ArrayList;
import java.util.List;

public class ModelImpl implements Model {

    private final List<ModelObserver> observers;
    private final SimulationState state;

    public ModelImpl() {
        state = new SimulationState();
        observers = new ArrayList<>();
    }

    @Override
    public void update() {
        /* update bodies velocity */

        for (Body b : state.getBodies()) {
            /* compute total force on bodies */
            V2d totalForce = computeTotalForceOnBody(b);
            /* compute instant acceleration */
            V2d acc = new V2d(totalForce).scalarMul(1.0 / b.getMass());
            /* update velocity */
            b.updateVelocity(acc, state.getDt());
        }

        /* compute bodies new pos */
        for (Body b : state.getBodies()) {
            b.updatePos(state.getDt());
        }

        /* check collisions with boundaries */
        for (Body b : state.getBodies()) {
            b.checkAndSolveBoundaryCollision(state.getBounds());
        }

        /* update virtual time */
        state.setVt(state.getVt() + state.getDt());

        /* display current stage */
        notifyObserver();
    }

    @Override
    public SimulationState getState() {
        return this.state;
    }

    @Override
    public void addObserver(ModelObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void notifyObserver() {
        for (ModelObserver observer: observers){
            observer.modelUpdated(this);
        }
    }

    private V2d computeTotalForceOnBody(Body b) {

        V2d totalForce = new V2d(0, 0);

        /* compute total repulsive force */

        for (int j = 0; j < state.getBodies().size(); j++) {
            Body otherBody = state.getBodies().get(j);
            if (!b.equals(otherBody)) {
                try {
                    V2d forceByOtherBody = b.computeRepulsiveForceBy(otherBody);
                    totalForce.sum(forceByOtherBody);
                } catch (Exception ignored) {
                }
            }
        }

        /* add friction force */
        totalForce.sum(b.getCurrentFrictionForce());

        return totalForce;
    }
}