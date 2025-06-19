package ru.practicum.kanban.manager;

import ru.practicum.kanban.interfaces.HistoryManager;
import ru.practicum.kanban.tasks.Node;
import ru.practicum.kanban.tasks.Task;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer, Node> mapHistory = new HashMap<>();

    private Node head = null;
    private Node tail = null;

    public void linkLast(Task task) {
    if (task == null) return;
    Node node = new Node(task);
        if (mapHistory.isEmpty()) {
            head = node;
            tail = node;
            mapHistory.put(task.getIdNum(), node);
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
            mapHistory.put(task.getIdNum(), node);
        }
    }

    public List<Task> getTasks() {
        List<Task> tasksList = new ArrayList<>();
        mapHistory.values()
            .forEach(i -> tasksList.add(i.getTask()));
        return tasksList;
    }

    public void removeNode(Node node) {
        if (node == null) return;
        if (node == head) {
            head = node.next;
        } else {
            node.prev.next = node.next;
        }
        if (node == tail) {
            tail = node.prev;
        } else {
            node.next.prev = node.prev;
        }
        Task task = node.getTask();
        int taskId = task.getIdNum();
        mapHistory.remove(taskId);
    }

    @Override
    public void add(Task task) {
        if (task == null) return;
        int taskId = task.getIdNum();
        mapHistory.remove(taskId);
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        removeNode(mapHistory.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}
