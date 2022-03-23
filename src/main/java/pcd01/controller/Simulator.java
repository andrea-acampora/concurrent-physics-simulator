package pcd01.controller;

import pcd01.model.Model;
import pcd01.utils.Chrono;

public class Simulator implements Controller {

	private final Model model;

	public Simulator(Model model) {
		this.model = model;
	}
	
	public void execute(long maxSteps) {

		/* simulation loop */

		Chrono chrono = new Chrono();

		chrono.start();
		while (model.getState().getSteps() < maxSteps) {
			model.update();
			model.getState().incrementSteps();
		}
		chrono.stop();
		System.out.println("Time elapsed: " + chrono.getTime() / 1000+ " seconds.");

	}
}
