package main.kanban1.java.src.Interfaces;

import main.kanban1.java.src.tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    List<Task> getHistory();
}
