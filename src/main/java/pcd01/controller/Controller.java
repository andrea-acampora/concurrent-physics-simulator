package pcd01.controller;

import pcd01.controller.concurrent.Flag;
import pcd01.controller.concurrent.MasterAgentOneBodyPerTask;
import pcd01.controller.concurrent.MasterAgentSubListPerTask;
import pcd01.controller.concurrent.StartSynch;
import pcd01.model.Model;
import pcd01.model.ModelImpl;
import pcd01.view.SimulationView;
import pcd01.view.View;

public class Controller implements InputListener {


	private Model model;
	private View view;
	private MasterAgentOneBodyPerTask masterAgentOneBodyPerTask;
	//private MasterAgentSubListPerTask masterAgentSubListPerTask;
	private final Flag stopFlag;
	private final StartSynch startSynch;

	private int numberOfSteps = 1000;

	public Controller(final Model model, final View view) {
		this.model = model;
		this.view = view;
		this.view.addListener(this);
		this.stopFlag = new Flag();
		this.startSynch = new StartSynch();
		this.masterAgentOneBodyPerTask = new MasterAgentOneBodyPerTask(view, model.getState(), numberOfSteps, stopFlag, startSynch );
		this.masterAgentOneBodyPerTask.start();
		// this.masterAgentSubListPerTask = new MasterAgentSubListPerTask(view, model.getState(), numberOfSteps, stopFlag, startSynch);
		// this.masterAgentSubListPerTask.start();
	}

	public synchronized void start() {
		stopFlag.reset();
		startSynch.notifyStarted();
	}

	public synchronized void stop() {
		startSynch.reset();
		stopFlag.set();
	}

}
