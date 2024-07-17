package manager;

import main.kanban1.java.src.Interfaces.HistoryManager;
import main.kanban1.java.src.Interfaces.TaskManager;
import main.kanban1.java.src.manager.InMemoryHistoryManager;
import main.kanban1.java.src.tasks.Epic;
import main.kanban1.java.src.tasks.Node;
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
    Subtask subtask;
    Epic epic;
    Managers managers;
    TaskManager taskManager;
    HistoryManager historyManager;
    InMemoryHistoryManager historyManagerClassVar;

    @BeforeEach
    public void beforeEach() {
        task = new Task("task","to do something");
        task0 = new Task("task","to do something");
        subtask = new Subtask("subtask","to do something");
        subtask.setIdNum(123);
        epic = new Epic("epic","to do something");
        managers = new Managers();
        taskManager = managers.getDefault();
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
    void linkLastMethodTest() {
        task.setIdNum(1);
        task0.setIdNum(2);
        subtask.setIdNum(3);
        historyManagerClassVar.linkLast(task);
        historyManagerClassVar.linkLast(task0);
        historyManagerClassVar.linkLast(subtask);
        Assertions.assertEquals(historyManagerClassVar.mapHistory.size(),3);
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
    void removeNodeMethodTest() {
        task.setIdNum(1);
        task0.setIdNum(2);
        subtask.setIdNum(3);
        epic.setIdNum(4);
        historyManagerClassVar.linkLast(task);
        historyManagerClassVar.linkLast(task0);
        historyManagerClassVar.linkLast(subtask);
        historyManagerClassVar.linkLast(epic);
        Node deletedNode = historyManagerClassVar.mapHistory.get(3);
        historyManagerClassVar.removeNode(deletedNode);
        Assertions.assertEquals(historyManagerClassVar.mapHistory.get(3),null);
    }

    @Test
    void removeNodeMethodChangeLinksCorrectly() {
        task.setIdNum(1);
        task0.setIdNum(2);
        subtask.setIdNum(3);
        epic.setIdNum(4);
        historyManagerClassVar.linkLast(task);
        historyManagerClassVar.linkLast(task0);
        historyManagerClassVar.linkLast(subtask);
        historyManagerClassVar.linkLast(epic);
        Node deletedNode = historyManagerClassVar.mapHistory.get(4);
        historyManagerClassVar.removeNode(deletedNode);
        Assertions.assertEquals(historyManagerClassVar.getTail(), historyManagerClassVar.mapHistory.get(3));
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
}