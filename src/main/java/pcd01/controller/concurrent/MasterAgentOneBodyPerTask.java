package pcd01.controller.concurrent;

import gov.nasa.jpf.vm.Verify;
import pcd01.model.*;
import java.util.Collections;

public class MasterAgentOneBodyPerTask extends AbstractMasterAgent {

    public MasterAgentOneBodyPerTask(SimulationState state, long maxSteps) {
        super(state, maxSteps);
        this.taskLatch = new TaskCompletionLatch(state.getBodies().size());
    }

    @Override
    void addComputeForcesTasksToBag() {
        Verify.beginAtomic();
        state.getBodies().forEach( b -> taskBag.addNewTask(taskFactory.createComputeForcesTask(state, Collections.singletonList(b))));
        Verify.endAtomic();
    }

    @Override
    void addUpdatePositionTasksToBag() {
        Verify.beginAtomic();
        state.getBodies().forEach( b -> taskBag.addNewTask(taskFactory.createUpdatePositionTask(state, Collections.singletonList(b))));
        Verify.endAtomic();
    }
}
