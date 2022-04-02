package pcd01.controller.concurrent;

import pcd01.model.*;
import pcd01.view.View;

import java.util.List;

public class MasterAgentOneBodyPerTask extends AbstractMasterAgent {

    public MasterAgentOneBodyPerTask(View view, SimulationState state, long maxSteps, Flag stopFlag, StartSynch synch) {
        super(view, state, maxSteps, stopFlag, synch);
        this.taskLatch = new TaskCompletionLatch(state.getBodies().size());
    }

    @Override
    void addComputeForcesTasksToBag() {
        state.getBodies().forEach( b -> taskBag.addNewTask(taskFactory.createComputeForcesTask(state, List.of(b))));
    }

    @Override
    void addUpdatePositionTasksToBag() {
        state.getBodies().forEach( b -> taskBag.addNewTask(taskFactory.createUpdatePositionTask(state, List.of(b))));
    }
}
