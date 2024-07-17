package main.kanban1.java.src.manager;

import main.kanban1.java.src.Interfaces.HistoryManager;
import main.kanban1.java.src.tasks.Node;
import main.kanban1.java.src.tasks.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> watchHistory = new ArrayList<>();
    public Map<Integer, Node> mapHistory = new HashMap<>();

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
        for (Node node : mapHistory.values()) {
            Task task = (Task) node.getTask();
            tasksList.add(task);
        }
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
        Task task = (Task) node.getTask();
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
        watchHistory = getHistory();
    }

    @Override
    public List<Task> getHistory() {
        List<Task> copyOfWatchHistory = getTasks();
        return copyOfWatchHistory;
    }

    public Node getTail() {
        return this.tail;
    }

    public Node getHead() {
        return this.head;
    }
}
