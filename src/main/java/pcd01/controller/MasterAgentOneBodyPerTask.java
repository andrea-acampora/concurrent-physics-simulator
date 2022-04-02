package pcd01.controller;

import com.google.common.collect.Lists;
import pcd01.application.Main;
import pcd01.model.*;
import pcd01.utils.Chrono;
import pcd01.view.View;

import java.util.List;
import java.util.stream.IntStream;

public class MasterAgentOneBodyPerTask extends AbstractMasterAgent {

    public MasterAgentOneBodyPerTask(View view, SimulationState state, long maxSteps, Flag stopFlag, StartSynch synch) {
        super(view, state, maxSteps, stopFlag, synch);
    }

    @Override
    void addComputeForcesTasksToBag() {
        state.getBodies().forEach( b -> taskBag.addNewTask(taskFactory.createComputeForcesTask(state, b)));
    }

    @Override
    void addUpdatePositionTasksToBag() {
        state.getBodies().forEach( b -> taskBag.addNewTask(taskFactory.createUpdatePositionTask(state, b)));
    }
}
