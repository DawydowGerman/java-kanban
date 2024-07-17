package main.kanban1.java.src.tasks;

public class Node<Task> {
    private Task task;
    public Node<Task> next;
    public Node<Task> prev;

    public Node(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return this.task;
    }
}
