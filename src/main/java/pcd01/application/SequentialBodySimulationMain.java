package pcd01.application;

import pcd01.controller.Controller;
import pcd01.controller.Simulator;
import pcd01.model.Model;
import pcd01.model.ModelImpl;
import pcd01.view.SimulationView;
import pcd01.view.View;

/**
 * Bodies simulation - legacy code: sequential, unstructured
 * 
 * @author aricci
 */
public class SequentialBodySimulationMain {

    public static void main(String[] args) {

        Model model = new ModelImpl();
        Controller controller = new Simulator(model);
        View view = new SimulationView(controller);
        model.addObserver(view);
        view.start();
        controller.execute(5000);
    }
}
