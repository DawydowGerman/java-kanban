package ru.practicum.kanban.tasks;

public class Node {
    private Task task;
    private Node next;
    private Node prev;

    public Node(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return this.task;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }
}