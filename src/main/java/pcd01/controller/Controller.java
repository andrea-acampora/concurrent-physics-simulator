package pcd01.controller;

import pcd01.controller.concurrent.MasterAgentOneBodyPerTask;

import pcd01.model.Model;


public class Controller {


	private Model model;
	private MasterAgentOneBodyPerTask masterAgentOneBodyPerTask;

	private int numberOfSteps = 2;

	public Controller(final Model model) {
		this.model = model;
		this.masterAgentOneBodyPerTask = new MasterAgentOneBodyPerTask(model.getState(), numberOfSteps );
		this.masterAgentOneBodyPerTask.start();
	}
}
