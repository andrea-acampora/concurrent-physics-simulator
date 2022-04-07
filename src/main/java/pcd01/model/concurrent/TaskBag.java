package pcd01.model.concurrent;

import java.util.LinkedList;

public class TaskBag {
    private LinkedList<Task> buffer;

    public TaskBag() {
        this.buffer = new LinkedList<Task>();
    }

    public synchronized void clear() {
        buffer.clear();
    }

    public synchronized void addNewTask(Task task) {
        this.buffer.addLast(task);
        notifyAll();
    }

    public synchronized Task getATask() throws InterruptedException {
        while(buffer.isEmpty()){
                wait();
        }
        return buffer.removeFirst();
    }

    public synchronized int getSize() {
        return this.buffer.size();
    }
}
