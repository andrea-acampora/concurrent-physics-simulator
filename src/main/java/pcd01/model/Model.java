package pcd01.model;

public interface Model {

    void update();

    SimulationState getState();

    void addObserver(ModelObserver observer);

    void notifyObserver();
}
