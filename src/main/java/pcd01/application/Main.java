package pcd01.application;

import pcd01.controller.*;
import pcd01.model.Model;
import pcd01.model.ModelImpl;
import pcd01.view.SimulationView;
import pcd01.view.View;

public class Main {

    public static void main(String[] args) {
        int numberOfBodies = 500;
        Model model = new ModelImpl(numberOfBodies);
        View view = new SimulationView();
        Controller controller = new Controller(model, view);
        view.start();
    }
}
