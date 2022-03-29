package pcd01.controller;

import pcd01.model.Model;
import pcd01.utils.Chrono;
import pcd01.view.SimulationView;
import pcd01.view.View;

public class Simulator implements Controller {

	private final Model model;
	private final View view;

	public Simulator(Model model, View view) {
		this.model = model;
		this.view = view;
	}
	
	public void execute(long maxSteps) {
		/* simulation loop
		Chrono chrono = new Chrono();
		chrono.start();
		while (model.getState().getSteps() < maxSteps) {
			model.update();
			model.getState().incrementSteps();
		}
		chrono.stop();
		System.out.println("Time elapsed: " + chrono.getTime() / 1000+ " seconds.");*/

		int nWorker = Runtime.getRuntime().availableProcessors() + 1;
		new MasterAgent(view, model.getState(), maxSteps).start();

	}
}
