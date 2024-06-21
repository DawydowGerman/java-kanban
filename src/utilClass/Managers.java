package main.kanban1.java.src.utilClass;

import main.kanban1.java.src.Interfaces.HistoryManager;
import main.kanban1.java.src.manager.InMemoryHistoryManager;
import main.kanban1.java.src.manager.InMemoryTaskManager;
import main.kanban1.java.src.Interfaces.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
