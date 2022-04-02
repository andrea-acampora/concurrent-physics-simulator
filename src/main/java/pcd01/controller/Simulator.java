package pcd01.controller;

import pcd01.controller.concurrent.Flag;
import pcd01.controller.concurrent.StartSynch;

public class Simulator implements InputListener {

	private final Flag stopFlag;
	private final StartSynch synch;


	public Simulator(Flag stopFlag, StartSynch synch) {
		this.stopFlag = stopFlag;
		this.synch = synch;
	}

	@Override
	public synchronized void start() {
		stopFlag.reset();
		synch.notifyStarted();
	}

	@Override
	public synchronized void stop() {
		synch.reset();
		stopFlag.set();
	}
}
