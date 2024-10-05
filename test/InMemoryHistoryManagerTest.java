package main.kanban1.java.test;

import main.kanban1.java.src.Interfaces.HistoryManager;
import main.kanban1.java.src.manager.InMemoryHistoryManager;
import main.kanban1.java.src.tasks.Epic;
import main.kanban1.java.src.tasks.Subtask;
import main.kanban1.java.src.tasks.Task;
import main.kanban1.java.src.util.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

class InMemoryHistoryManagerTest {
    Task task;
    Task task0;
    Task task1;
    Task task2;

    Subtask subtask;
    Epic epic;
    Managers managers;
    HistoryManager historyManager;
    InMemoryHistoryManager historyManagerClassVar;

    @BeforeEach
    public void beforeEach() {
        task = new Task("task","to do something");
        task0 = new Task("task","to do something");
        task1 = new Task("task","to do something");
        task2 = new Task("task","to do something");

        subtask = new Subtask("subtask","to do something");
        subtask.setIdNum(123);
        epic = new Epic("epic","to do something");
        managers = new Managers();
        historyManager = managers.getDefaultHistory();
        historyManagerClassVar = new InMemoryHistoryManager();
    }

    @Test
    void tasksAddedToWatchHistoryKeepFieldsImmutable() {
        historyManager.add(task);
        Assertions.assertEquals(task, task0);
    }

    @Test
    void removeMethodTest() {
        task.setIdNum(1);
        task0.setIdNum(2);
        subtask.setIdNum(3);
        epic.setIdNum(4);
        historyManagerClassVar.add(task);
        historyManagerClassVar.add(task0);
        historyManagerClassVar.add(subtask);
        historyManagerClassVar.add(epic);
        historyManagerClassVar.remove(2);
        List<Task> taskList = historyManagerClassVar.getHistory();
        Assertions.assertEquals(taskList.size(), 3);
    }

    @Test
    void getTasksMethodTest() {
        task.setIdNum(1);
        task0.setIdNum(2);
        subtask.setIdNum(3);
        historyManagerClassVar.linkLast(task);
        historyManagerClassVar.linkLast(task0);
        historyManagerClassVar.linkLast(subtask);
        List<Task> tasksList = historyManagerClassVar.getTasks();
        Assertions.assertEquals(tasksList.size(),3);
    }

    @Test
    void addMethodDoesntAllowDups() {
        task.setIdNum(1);
        task0.setIdNum(2);
        subtask.setIdNum(3);
        epic.setIdNum(4);
        historyManagerClassVar.add(task);
        historyManagerClassVar.add(task0);
        historyManagerClassVar.add(subtask);
        historyManagerClassVar.add(epic);
        historyManagerClassVar.add(task);
        List<Task> tasksList = historyManagerClassVar.getTasks();
        Assertions.assertEquals(tasksList.size(), 4);
    }

    @Test
    void getHistoryMethodsTest() {
        historyManager.add(task);
        List<Task> list = historyManager.getHistory();
        Assertions.assertEquals(list.size(), 1);
    }

    @Test
    void emptyMapHistoryTest() {
        task.setIdNum(34);
        historyManager.add(task);
        historyManager.remove(34);
        List<Task> list = historyManager.getHistory();
        Assertions.assertEquals(list.size(), 0);
    }

    @Test
    void dupsTest() {
        task.setIdNum(1);
        task0.setIdNum(1);
        historyManager.add(task);
        historyManager.add(task0);
        List<Task> list = historyManager.getHistory();
        Assertions.assertEquals(list.size(), 1);
    }

    @Test
    void removeFromTheBeginning() {
        task.setIdNum(0);
        task0.setIdNum(1);
        task1.setIdNum(2);
        task2.setIdNum(3);
        historyManager.add(task);
        historyManager.add(task0);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(0);
        List<Task> list = historyManager.getHistory();
        Task task = list.get(0);
        Assertions.assertEquals(task, task0);
    }

    @Test
    void removeInTheMiddle() {
        task.setIdNum(0);
        task0.setIdNum(1);
        task1.setIdNum(2);
        task2.setIdNum(3);
        historyManager.add(task);
        historyManager.add(task0);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(1);
        List<Task> list = historyManager.getHistory();
        Task task = list.get(1);
        Assertions.assertEquals(task, task1);
    }

    @Test
    void removeAtTheEnd() {
        task.setIdNum(0);
        task0.setIdNum(1);
        task1.setIdNum(2);
        task2.setIdNum(3);
        historyManager.add(task);
        historyManager.add(task0);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(3);
        List<Task> list = historyManager.getHistory();
        Task task = list.get(2);
        Assertions.assertEquals(task, task1);
    }

}
