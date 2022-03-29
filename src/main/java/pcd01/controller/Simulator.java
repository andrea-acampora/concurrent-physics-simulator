package pcd01.controller;

import pcd01.model.Model;
import pcd01.view.View;

public class Simulator implements Controller {

	private final Model model;
	private final View view;

	public Simulator(Model model, View view) {
		this.model = model;
		this.view = view;
	}
	
	public void execute(long maxSteps) {
		/* simulation loop*/
		new MasterAgent(view, model.getState(), maxSteps).start();
	}
}
