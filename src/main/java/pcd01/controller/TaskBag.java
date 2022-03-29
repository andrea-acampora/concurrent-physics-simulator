package pcd01.controller;

import java.util.LinkedList;

public class TaskBag {
    private LinkedList<Task> buffer;

    public TaskBag() {
        this.buffer = new LinkedList<Task>();
    }

    public synchronized void clear(){
        buffer.clear();
    }

    public synchronized void addNewTask(Task task){
        this.buffer.addLast(task);
        notifyAll();
    }

    public synchronized Task getATask(){
        while(buffer.isEmpty()){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return buffer.removeFirst();
    }
}
