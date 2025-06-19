package ru.practicum.kanban.tasks;

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