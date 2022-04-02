package pcd01.application;

import pcd01.controller.*;
import pcd01.controller.concurrent.Flag;
import pcd01.controller.concurrent.MasterAgentOneBodyPerTask;
import pcd01.controller.concurrent.MasterAgentSubListPerTask;
import pcd01.controller.concurrent.StartSynch;
import pcd01.model.Model;
import pcd01.model.ModelImpl;
import pcd01.view.SimulationView;
import pcd01.view.View;

public class Main {

    public static final boolean VIEW_ENABLED = true;


    public static void main(String[] args) {
        int numberOfSteps = 500;
        int numberOfBodies = 500;
        Flag stopFlag = new Flag();
        StartSynch synch = new StartSynch();
        Model model = new ModelImpl(numberOfBodies);
        View view = new SimulationView();
        Simulator controller = new Simulator(stopFlag, synch);
        //new MasterAgentOneBodyPerTask(view, model.getState(), numberOfSteps, stopFlag, synch).start();
        new MasterAgentSubListPerTask(view, model.getState(), numberOfSteps, stopFlag, synch).start();
        if (VIEW_ENABLED){
            view.addListener(controller);
            view.start();
        }
    }
}
