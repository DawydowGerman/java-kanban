package main.kanban1.java.test;

import main.kanban1.java.src.Interfaces.HistoryManager;
import main.kanban1.java.src.Interfaces.TaskManager;
import main.kanban1.java.src.status.Status;
import main.kanban1.java.src.tasks.Epic;
import main.kanban1.java.src.tasks.Subtask;
import main.kanban1.java.src.tasks.Task;
import main.kanban1.java.src.util.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class InMemoryTaskManagerTest {
    Task task;
    Task task0;
    Task task1;
    Task taskforsecondtest;
    Subtask subtask;
    Subtask subtask0;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;
    Subtask subtask4;
    Subtask subtask5;
    Subtask subtask6;
    Epic epic;
    Epic epic0;
    Epic epic1;
    Epic epic2;
    Epic epic3;
    Managers managers;
    TaskManager taskManager;
    TaskManager taskManager0;
    HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        task = new Task("task","to do something");
        task0 = new Task("task","to do something");
        task1 = new Task("task","to do something other");
        taskforsecondtest = new Task("task","to do something");

        subtask = new Subtask("task","to do something");
        subtask0 = new Subtask("task","to do something other");

        subtask1 = new Subtask("task","to do something as subtask1");
        subtask2 = new Subtask("task","to do something other as subtask2");

        subtask3 = new Subtask("task","to do something as subtask3");
        subtask4 = new Subtask("task","to do something other as subtask4");

        subtask5 = new Subtask("task","to do something as subtask5");
        subtask6 = new Subtask("task","to do something other as subtask6");

        epic = new Epic("task","to do something");
        epic0 = new Epic("task","to do something other");
        epic1 = new Epic("task","to do something other");
        epic2 = new Epic("task","to do something other");
        epic3 = new Epic("task","to do something other");


        managers = new Managers();
        taskManager = managers.getDefault();
        taskManager0 = managers.getDefault();

        historyManager = managers.getDefaultHistory();
    }

    @Test
    void tasksWithSettedAndGeneratedIdsDontConflict() {
        taskManager.addTaskObj(task);
        taskManager.addTaskObj(task0);
        task0.setIdNum(1);
        ArrayList<Task> list = taskManager.getTasks();
        Assertions.assertEquals(list.size(), 2);
    }

    @Test
    void immutabilityOfTasksFieldsAfterAdditionToManager() {
        taskManager.addTaskObj(taskforsecondtest);
        task0.setIdNum(13);
        Assertions.assertEquals(taskforsecondtest, task0);
    }

    @Test
    void getTasksMethodTest() {
        taskManager.addTaskObj(task);
        ArrayList<Task> list = taskManager.getTasks();
        Assertions.assertEquals(list.size(), 1);
    }

    @Test
    void getSubtasksMethodTest() {
        taskManager.addSubtaskObj(subtask);
        ArrayList<Subtask> list = taskManager.getSubtasks();
        Assertions.assertEquals(list.size(), 1);
    }

    @Test
    void getEpicsMethodTest() {
        taskManager.addEpicObj(epic);
        ArrayList<Epic> list = taskManager.getEpics();
        Assertions.assertEquals(list.size(), 1);
    }

    @Test
    void deleteAllTasksMethodTest() {
        taskManager.addTaskObj(task);
        taskManager.deleteAllTasks();
        ArrayList<Task> list = taskManager.getTasks();
        Assertions.assertEquals(list.size(), 0);
    }

    @Test
    void deleteAllSubtasksMethodTest() {
        taskManager.addSubtaskObj(subtask);
        taskManager.deleteAllSubtasks();
        ArrayList<Subtask> list = taskManager.getSubtasks();
        Assertions.assertEquals(list.size(), 0);
    }

    @Test
    void deleteAllEpicsMethodTest() {
        taskManager.addEpicObj(epic);
        taskManager.deleteAllEpics();
        ArrayList<Epic> list = taskManager.getEpics();
        Assertions.assertEquals(list.size(), 0);
    }

    @Test
    void getTaskByIdMethodTest() {
        taskManager.addTaskObj(task);
        Task task0 = taskManager.getTaskById(27);
        Assertions.assertEquals(task0, task);
    }

    @Test
    void getSubtaskByIdMethodTest() {
        taskManager.addSubtaskObj(subtask);
        Subtask subtask0 = taskManager.getSubtaskById(40);
        Assertions.assertEquals(subtask0, subtask);
    }

    @Test
    void getEpicByIdMethodTest() {
        taskManager.addEpicObj(epic);
        Epic epic0 = taskManager.getEpicById(7);
        Assertions.assertEquals(epic0, epic);
    }

    @Test
    void addTaskObjMethodTest() {
        taskManager.addTaskObj(task);
        ArrayList<Task> list = taskManager.getTasks();
        Assertions.assertEquals(list.size(), 1);
    }

    @Test
    void addSubtaskObjMethodTest() {
        taskManager.addSubtaskObj(subtask);
        ArrayList<Subtask> list = taskManager.getSubtasks();
        Assertions.assertEquals(list.size(), 1);
    }

    @Test
    void addEpicObjMethodTest() {
        taskManager.addEpicObj(epic);
        ArrayList<Epic> list = taskManager.getEpics();
        Assertions.assertEquals(list.size(), 1);
    }

    @Test
    void updTaskMethodTest() {
        taskManager.addTaskObj(task);
        taskManager.addTaskObj(task1);
        task1.setIdNum(1);
        taskManager.updTask(task1);
        Assertions.assertEquals(task1.getDescription(), "to do something other");
    }

    @Test
    void updSubtaskMethodTest() {
        taskManager.addSubtaskObj(subtask);
        taskManager.addSubtaskObj(subtask0);
        subtask0.setIdNum(1);
        taskManager.updSubtask(subtask0);
        Assertions.assertEquals(subtask0.getDescription(), "to do something other");
    }

    @Test
    void updEpicMethodTest() {
        taskManager.addEpicObj(epic);
        taskManager.addEpicObj(epic0);
        epic0.setIdNum(1);
        taskManager.updEpic(epic0);
        Assertions.assertEquals(epic0.getDescription(), "to do something other");
    }

    @Test
    void deleteTaskByIdMethodTest() {
        taskManager.addTaskObj(task);
        taskManager.deleteTaskById(1);
        Assertions.assertEquals(taskManager.getTaskById(1), null);
    }

    @Test
    void deleteTaskByIdMethodRemovesFromHistoryTest() {
        taskManager.addTaskObj(task);
        taskManager.getTaskById(1);
        taskManager.deleteTaskById(1);
        List<Task> list = historyManager.getHistory();
        Assertions.assertEquals(list.size(), 0);
    }

    @Test
    void deleteSubtaskByIdMethodTest() {
        taskManager.addEpicObj(epic);
        taskManager.addSubtaskObj(subtask);
        subtask.setEpicId(epic.getIdNum());
        taskManager.deleteSubtaskById(subtask.getIdNum());
        Assertions.assertEquals(taskManager.getSubtaskById(2), null);
    }

    @Test
    void deleteSubtaskByIdMethodRemovesFromHistoryTest() {
        taskManager.addEpicObj(epic);
        taskManager.addSubtaskObj(subtask);
        subtask.setEpicId(epic.getIdNum());
        taskManager.getSubtaskById(2);
        taskManager.deleteSubtaskById(1);
        List<Task> list = historyManager.getHistory();
        Assertions.assertEquals(list.size(), 0);
    }

    @Test
    void deleteEpicByIdMethodTest() {
        taskManager.addEpicObj(epic);
        taskManager.deleteEpicById(1);
        Assertions.assertEquals(taskManager.getEpicById(1), null);
    }

    @Test
    void deleteEpicByIdMethodRemovesFromHistoryTest() {
        taskManager.addEpicObj(epic);
        taskManager.addSubtaskObj(subtask);
        epic.linkSubtaskToEpic(subtask.getIdNum());
        taskManager.getEpicById(epic.getIdNum());
        taskManager.deleteEpicById(1);
        List<Task> list = historyManager.getHistory();
        Assertions.assertEquals(list.size(), 0);
    }

    @Test
    void getAllSubtasksOfOneEpicMethodTest() {
        epic.linkSubtaskToEpic(1);
        epic.linkSubtaskToEpic(2);
        taskManager.addSubtaskObj(subtask);
        taskManager.addSubtaskObj(subtask0);
        ArrayList<Subtask> list = taskManager.getAllSubtasksOfOneEpic(epic);
        Assertions.assertEquals(list.size(), 2);
    }

    @Test
    void updateEpicStatusWhenAllSubtasksNEW() {
        taskManager0.addSubtaskObj(subtask1);
        taskManager0.addSubtaskObj(subtask2);
        taskManager0.addEpicObj(epic1);
        epic1.linkSubtaskToEpic(subtask1.getIdNum());
        epic1.linkSubtaskToEpic(subtask2.getIdNum());
        taskManager0.updateEpicStatus(epic1); // here is the issue
        Assertions.assertEquals(epic1.getStatus(), Status.NEW);
    }

    @Test
    void updateEpicStatusWhenAllSubtasksDONE() {
        taskManager0.addSubtaskObj(subtask3);
        taskManager0.addSubtaskObj(subtask4);
        taskManager0.addEpicObj(epic2);
        subtask3.setStatus(Status.DONE);
        subtask4.setStatus(Status.DONE);
        epic2.linkSubtaskToEpic(subtask3.getIdNum());
        epic2.linkSubtaskToEpic(subtask4.getIdNum());
        taskManager0.updateEpicStatus(epic2);
        Assertions.assertEquals(epic2.getStatus(), Status.DONE);
    }

    @Test
    void updateEpicStatusWhenAllSubtasksIN_PROGRESS() {
        taskManager0.addSubtaskObj(subtask5);
        taskManager0.addSubtaskObj(subtask6);
        taskManager0.addEpicObj(epic3);
        subtask5.setStatus(Status.IN_PROGRESS);
        subtask6.setStatus(Status.DONE);
        epic3.linkSubtaskToEpic(subtask5.getIdNum());
        epic3.linkSubtaskToEpic(subtask6.getIdNum());
        taskManager0.updateEpicStatus(epic3);
        Assertions.assertEquals(epic3.getStatus(), Status.IN_PROGRESS);
    }
}