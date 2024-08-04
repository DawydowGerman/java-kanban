package main.kanban1.java.src.tasks;

public class Node {
    private Task task;
    public Node next;
    public Node prev;

    public Node(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return this.task;
    }
}
