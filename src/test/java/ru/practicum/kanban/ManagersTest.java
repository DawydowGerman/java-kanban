package ru.practicum.kanban;

import ru.practicum.kanban.interfaces.HistoryManager;
import ru.practicum.kanban.interfaces.TaskManager;
import ru.practicum.kanban.tasks.Task;
import ru.practicum.kanban.util.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

class ManagersTest {
    Task task;
    Managers managers;
    TaskManager taskManager;
    HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        task = new Task("task","to do something");
        task.setStartTime(2024,3,21,12,34);
        task.setDuration(60);
        managers = new Managers();
        taskManager = managers.getDefault();
        taskManager.addTaskObj(task);
        historyManager = managers.getDefaultHistory();
    }

    @Test
    void getDefaultMethodReturnsReadyForUseInMemoryTaskManager() {
        List<Task> listOfTasks = taskManager.getTasks();
        Assertions.assertEquals(listOfTasks.size(), 1);
    }

    @Test
    void getDefaultHistoryMethodReturnsReadyForUseInMemoryHistoryManager() {
        historyManager.add(task);
        List<Task> listOfTasks = historyManager.getHistory();
        Assertions.assertEquals(listOfTasks.size(), 1);
    }
}