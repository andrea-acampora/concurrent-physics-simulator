package pcd01.controller;

import pcd01.model.Model;
import pcd01.view.View;

public class Simulator implements InputListener {

//	private final Model model;
//	private final View view;
	private final Flag stopFlag;
	private final StartSynch synch;


	public Simulator(Flag stopFlag, StartSynch synch) {
		this.stopFlag = stopFlag;
		this.synch = synch;
	}
	
	/* public void execute(long maxSteps) {
		new MasterAgent(view, model.getState(), maxSteps).start();
	}*/

	@Override
	public synchronized void started() {
		stopFlag.reset();
		synch.notifyStarted();
	}

	@Override
	public synchronized void stopped() {
		synch.reset();
		stopFlag.set();
	}
}
