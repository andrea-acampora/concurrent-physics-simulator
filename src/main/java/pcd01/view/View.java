package pcd01.view;

import pcd01.controller.InputListener;
import pcd01.model.SimulationState;

public interface View {

    void display(SimulationState state);

    void start();

    void addListener(InputListener listener);
}
