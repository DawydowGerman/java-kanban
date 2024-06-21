package main.kanban1.java.src.manager;

import main.kanban1.java.src.Interfaces.HistoryManager;
import main.kanban1.java.src.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import main.kanban1.java.src.tasks.Epic;
import main.kanban1.java.src.status.Status;
import main.kanban1.java.src.tasks.Subtask;
import main.kanban1.java.src.tasks.Task;

public class InMemoryHistoryManager implements HistoryManager {
    private static List<Task> watchHistory = new ArrayList<>();

    @Override
    public void add(Task task) {
        if(watchHistory.size() >= 10) {
            watchHistory.remove(0);
        }
        watchHistory.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return watchHistory;
    }
}
