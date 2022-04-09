package pcd01.controller.concurrent;

import pcd01.model.*;
import pcd01.model.concurrent.AbstractTaskFactory;
import pcd01.view.View;
import java.util.Collections;

public class MasterAgentOneBodyPerTask extends AbstractMasterAgent {

    public MasterAgentOneBodyPerTask(View view, SimulationState state, AbstractTaskFactory taskFactory, long maxSteps, StopFlag stopFlag, StartSynch startSynch) {
        super(view, state, taskFactory, maxSteps, stopFlag, startSynch);
        this.taskLatch = new TaskCompletionLatch(state.getBodies().size());
    }

    @Override
    void addComputeForcesTasksToBag() {
        state.getBodies().forEach( b -> taskBag.addNewTask(taskFactory.createComputeForcesTask(state, Collections.singletonList(b))));
    }

    @Override
    void addUpdatePositionTasksToBag() {
        state.getBodies().forEach( b -> taskBag.addNewTask(taskFactory.createUpdatePositionTask(state, Collections.singletonList(b))));
    }
}
