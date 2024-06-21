package manager;

import main.kanban1.java.src.Interfaces.HistoryManager;
import main.kanban1.java.src.Interfaces.TaskManager;
import main.kanban1.java.src.tasks.Epic;
import main.kanban1.java.src.tasks.Subtask;
import main.kanban1.java.src.tasks.Task;
import main.kanban1.java.src.utilClass.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    Task task;
    Task task0;
    Subtask subtask;
    Epic epic;
    Managers managers;
    TaskManager taskManager;
    HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        task = new Task("task","to do something");
        task0 = new Task("task","to do something");
        subtask = new Subtask("task","to do something");
        epic = new Epic("task","to do something");
        managers = new Managers();
        taskManager = managers.getDefault();
        historyManager = managers.getDefaultHistory();
    }

    @Test
    void tasksAddedToWatchHistoryKeepFieldsImmutable() {
        historyManager.add(task);
        Assertions.assertEquals(task, task0);
    }

    @Test
    void addAndGetHistoryMethodsTest() {
        historyManager.add(task);
        List<Task> list = historyManager.getHistory();
        Assertions.assertEquals(list.size(), 1);
    }
}