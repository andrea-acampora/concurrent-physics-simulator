package pcd01.controller.concurrent;

import com.google.common.collect.Lists;
import pcd01.model.Body;
import pcd01.model.SimulationState;
import pcd01.view.View;
import java.util.List;
import java.util.stream.IntStream;

public class MasterAgentSubListPerTask extends AbstractMasterAgent {
    private final List<List<Body>> bodiesSplit;

    public MasterAgentSubListPerTask(View view, SimulationState state, long maxSteps, Flag stopFlag, StartSynch startSynch) {
        super(view, state, maxSteps, stopFlag, startSynch);
        this.bodiesSplit = Lists.partition(state.getBodies(),  state.getBodies().size() / nWorker +1);
        this.taskLatch = new TaskCompletionLatch(nWorker);
    }

    @Override
    void addComputeForcesTasksToBag() {
        IntStream.range(0, nWorker).forEach((i) -> taskBag.addNewTask(taskFactory.createComputeForcesTask(state, bodiesSplit.get(i))));
    }

    @Override
    void addUpdatePositionTasksToBag() {
        IntStream.range(0, nWorker).forEach((i) -> taskBag.addNewTask(taskFactory.createUpdatePositionTask(state, bodiesSplit.get(i))));
    }
}
