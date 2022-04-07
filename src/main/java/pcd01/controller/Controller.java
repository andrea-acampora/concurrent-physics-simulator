package pcd01.controller;

import pcd01.controller.concurrent.MasterAgentOneBodyPerTask;

import pcd01.model.Model;

public class Controller {

	public Controller(final Model model) {
		new MasterAgentOneBodyPerTask(model.getState(), 1 ).start();
	}
}
