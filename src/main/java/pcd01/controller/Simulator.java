package pcd01.controller;

import pcd01.model.Model;

public class Simulator implements Controller {

	private final Model model;

	public Simulator(Model model) {
		this.model = model;
	}
	
	public void execute(long maxSteps) {

		/* simulation loop */
		while (model.getState().getSteps() < maxSteps) {
			model.update();
			model.getState().incrementSteps();
		}
	}
}
