package pcd01.controller;

import com.google.common.collect.Lists;
import pcd01.model.Body;
import pcd01.model.SimulationState;
import pcd01.view.View;
import java.util.List;
import java.util.stream.IntStream;

public class MasterAgentSubListPerTask extends AbstractMasterAgent {
    private final List<List<Body>> bodiesSplit;

    public MasterAgentSubListPerTask(View view, SimulationState state, long maxSteps, Flag stopFlag, StartSynch synch) {
        super(view, state, maxSteps, stopFlag, synch);
        this.bodiesSplit = Lists.partition(state.getBodies(),  state.getBodies().size() / nWorker +1);
    }

    @Override
    void addComputeForcesTasksToBag() {
        IntStream.range(0, nWorker).forEach((i) -> taskBag.addNewTask(taskFactory.createComputeForcesTask(state, bodiesSplit.get(i).toArray(new Body[0]))));
    }

    @Override
    void addUpdatePositionTasksToBag() {
        IntStream.range(0, nWorker).forEach((i) -> taskBag.addNewTask(taskFactory.createUpdatePositionTask(state, bodiesSplit.get(i).toArray(new Body[0]))));
    }
}
