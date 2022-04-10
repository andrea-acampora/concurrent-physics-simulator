------------------------------- MODULE ass01 -------------------------------
EXTENDS Integers, Sequences, TLC, FiniteSets

WorkerAgents == {"worker-1", "worker-2"}
BodySize == 10
MaxStep == 10

(*--algorithm ass01

variable
  bagOfTasks =  0,
  numStepDone = 0,
  numTaskDone = 0,
  simulationOver = FALSE,
  workerStarted = FALSE;

fair+ process master = "master"
  begin Start:
    while (numStepDone < MaxStep) do
      initialize:
        workerStarted := TRUE;
        bagOfTasks := BodySize;
        numTaskDone := 0;
      assignComputeForcesTask:
        await numTaskDone = BodySize;
        bagOfTasks := BodySize;
        numTaskDone := 0;
      assignUpdatePositionTask:  
        await numTaskDone = BodySize;
        numStepDone:= numStepDone + 1;
    end while;
    simulationOver := TRUE;
    print "simulation is over";
end process;

fair+ process worker \in WorkerAgents
  begin Compute:
    while simulationOver = FALSE do    
      take:
        await bagOfTasks > 0 \/ simulationOver = TRUE;
        bagOfTasks:= bagOfTasks - 1;
      compute:
        numTaskDone:= numTaskDone + 1;
    end while;       
  end process;
end algorithm;*)
\* BEGIN TRANSLATION (chksum(pcal) = "26083430" /\ chksum(tla) = "6f1d9211")
VARIABLES bagOfTasks, numStepDone, numTaskDone, simulationOver, workerStarted, 
          pc

vars == << bagOfTasks, numStepDone, numTaskDone, simulationOver, 
           workerStarted, pc >>

ProcSet == {"master"} \cup (WorkerAgents)

Init == (* Global variables *)
        /\ bagOfTasks = 0
        /\ numStepDone = 0
        /\ numTaskDone = 0
        /\ simulationOver = FALSE
        /\ workerStarted = FALSE
        /\ pc = [self \in ProcSet |-> CASE self = "master" -> "Start"
                                        [] self \in WorkerAgents -> "Compute"]

Start == /\ pc["master"] = "Start"
         /\ IF (numStepDone < MaxStep)
               THEN /\ pc' = [pc EXCEPT !["master"] = "initialize"]
                    /\ UNCHANGED simulationOver
               ELSE /\ simulationOver' = TRUE
                    /\ PrintT("simulation is over")
                    /\ pc' = [pc EXCEPT !["master"] = "Done"]
         /\ UNCHANGED << bagOfTasks, numStepDone, numTaskDone, workerStarted >>

initialize == /\ pc["master"] = "initialize"
              /\ workerStarted' = TRUE
              /\ bagOfTasks' = BodySize
              /\ numTaskDone' = 0
              /\ pc' = [pc EXCEPT !["master"] = "assignComputeForcesTask"]
              /\ UNCHANGED << numStepDone, simulationOver >>

assignComputeForcesTask == /\ pc["master"] = "assignComputeForcesTask"
                           /\ numTaskDone = BodySize
                           /\ bagOfTasks' = BodySize
                           /\ numTaskDone' = 0
                           /\ pc' = [pc EXCEPT !["master"] = "assignUpdatePositionTask"]
                           /\ UNCHANGED << numStepDone, simulationOver, 
                                           workerStarted >>

assignUpdatePositionTask == /\ pc["master"] = "assignUpdatePositionTask"
                            /\ numTaskDone = BodySize
                            /\ numStepDone' = numStepDone + 1
                            /\ pc' = [pc EXCEPT !["master"] = "Start"]
                            /\ UNCHANGED << bagOfTasks, numTaskDone, 
                                            simulationOver, workerStarted >>

master == Start \/ initialize \/ assignComputeForcesTask
             \/ assignUpdatePositionTask

Compute(self) == /\ pc[self] = "Compute"
                 /\ IF simulationOver = FALSE
                       THEN /\ pc' = [pc EXCEPT ![self] = "take"]
                       ELSE /\ pc' = [pc EXCEPT ![self] = "Done"]
                 /\ UNCHANGED << bagOfTasks, numStepDone, numTaskDone, 
                                 simulationOver, workerStarted >>

take(self) == /\ pc[self] = "take"
              /\ bagOfTasks > 0 \/ simulationOver = TRUE
              /\ bagOfTasks' = bagOfTasks - 1
              /\ pc' = [pc EXCEPT ![self] = "compute"]
              /\ UNCHANGED << numStepDone, numTaskDone, simulationOver, 
                              workerStarted >>

compute(self) == /\ pc[self] = "compute"
                 /\ numTaskDone' = numTaskDone + 1
                 /\ pc' = [pc EXCEPT ![self] = "Compute"]
                 /\ UNCHANGED << bagOfTasks, numStepDone, simulationOver, 
                                 workerStarted >>

worker(self) == Compute(self) \/ take(self) \/ compute(self)

(* Allow infinite stuttering to prevent deadlock on termination. *)
Terminating == /\ \A self \in ProcSet: pc[self] = "Done"
               /\ UNCHANGED vars

Next == master
           \/ (\E self \in WorkerAgents: worker(self))
           \/ Terminating

Spec == /\ Init /\ [][Next]_vars
        /\ SF_vars(master)
        /\ \A self \in WorkerAgents : SF_vars(worker(self))

Termination == <>(\A self \in ProcSet: pc[self] = "Done")

\* END TRANSLATION 
=============================================================================