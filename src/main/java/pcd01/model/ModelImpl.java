package pcd01.model;

public class ModelImpl implements Model {

    private final SimulationState state;

    public ModelImpl(final int numberOfBodies) {
        state = new SimulationState(numberOfBodies);
    }

    @Override
    public SimulationState getState() {
        return this.state;
    }

}
