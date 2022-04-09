package pcd01.controller;

import pcd01.controller.concurrent.StopFlag;
import pcd01.controller.concurrent.MasterAgentOneBodyPerTask;
import pcd01.controller.concurrent.StartSynch;
import pcd01.model.Model;
import pcd01.view.View;

public class ControllerImpl implements InputListener, Controller {

	private final Model model;
	private final View view;
	private final MasterAgentOneBodyPerTask masterAgentOneBodyPerTask;
 // private final MasterAgentSubListPerTask masterAgentSubListPerTask;
	private final StopFlag stopFlag;
	private final StartSynch startSynch;

	private static final int NUMBER_OF_STEPS = 1000;

	public ControllerImpl(final Model model, final View view) {
		this.model = model;
		this.view = view;
		this.view.addListener(this);
		this.stopFlag = new StopFlag();
		this.startSynch = new StartSynch();
		this.masterAgentOneBodyPerTask = new MasterAgentOneBodyPerTask(view, model.getState(), model.getTaskFactory(), NUMBER_OF_STEPS, stopFlag, startSynch );
		// this.masterAgentSubListPerTask = new MasterAgentSubListPerTask(view, model.getState(), numberOfSteps, stopFlag, startSynch);
	}

	public void start() {
		stopFlag.reset();
		startSynch.notifyStarted();
	}

	public void stop() {
		startSynch.reset();
		stopFlag.set();
	}

	@Override
	public void execute() {
		this.masterAgentOneBodyPerTask.start();
	 // this.masterAgentSubListPerTask.start();
	}
}
