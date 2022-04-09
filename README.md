STEP 1 (senza GUI) : 
design architettura: MVC dove però il model non ha observer (con gui la view diventa observer )
architettura concorrente: master-worker
Il controller al posto di fare partire la simulazione fa partire il master agent che poi internamente:
    1. crea e fa partire i worker agents che si mettono in wait sui task (bag of task vuota)
    2. crea i vari task della simulazione e li inserisce in una bag of tasks ( ogni task si riferisce ad un body es. ComputeForcesTask(Body b), UpdatePositionTask(Body b))
    3. aspetta che i worker finiscano il primo gruppo di task tramite un latch, resetta la bag e inserisce i nuovi task

STEP 2:
Nello step2 viene introdotto un monitor per gestire l'avvio della simulazione (StartSynchTask) ed una flag per gestire lo stop.
Il master agent parte subito ma rimane in wait sullo start e quando parte la simulazione la view notifica al controller lo start 
che fa lo notify sul monitor e quindi il master parte.
Il resto tutto uguale a parte che i worker avranno anche la flag Stop da controllare ad ogni ciclo.

STEP1 <--> MANDELBROT3
STEP2 <--> MANDELBROT4
