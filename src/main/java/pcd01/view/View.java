package pcd01.view;

import pcd01.controller.InputListener;
import pcd01.controller.Simulator;
import pcd01.model.Body;
import pcd01.model.Boundary;
import pcd01.model.ModelObserver;
import pcd01.model.SimulationState;

import java.beans.IndexedPropertyChangeEvent;
import java.util.ArrayList;

public interface View extends ModelObserver {

    void display(SimulationState state);

    void start();

    void addListener(InputListener listener);
}
