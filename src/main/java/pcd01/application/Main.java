package pcd01.application;

import pcd01.controller.*;
import pcd01.model.Model;
import pcd01.model.ModelImpl;
import pcd01.view.SimulationView;
import pcd01.view.View;

/**
 * Bodies simulation - legacy code: sequential, unstructured
 * 
 * @author aricci
 */
public class Main {

    public static void main(String[] args) {
        Flag stopFlag = new Flag();
        StartSynch synch = new StartSynch();
        Model model = new ModelImpl();
        View view = new SimulationView();
        Simulator controller = new Simulator(stopFlag, synch);
        // model.addObserver(view);
        new MasterAgent(view, model.getState(), 5000, stopFlag, synch).start();
        view.addListener(controller);
        view.start();
        //controller.execute(1000);
    }
}
