package manager;

import main.kanban1.java.src.Interfaces.TaskManager;
import main.kanban1.java.src.status.Status;
import main.kanban1.java.src.tasks.Epic;
import main.kanban1.java.src.tasks.Subtask;
import main.kanban1.java.src.tasks.Task;
import main.kanban1.java.src.utilClass.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    Task task;
    Task task0;
    Task task1;
    Subtask subtask;
    Subtask subtask0;
    Epic epic;
    Epic epic0;
    Managers managers;
    TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        task = new Task("task","to do something");
        task0 = new Task("task","to do something");
        task1 = new Task("task","to do something other");

        subtask = new Subtask("task","to do something");
        subtask0 = new Subtask("task","to do something other");

        epic = new Epic("task","to do something");
        epic0 = new Epic("task","to do something other");

        managers = new Managers();
        taskManager = managers.getDefault();
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
        taskManager.addTaskObj(task);
        task0.setIdNum(1);
        Assertions.assertEquals(task, task0);
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
        Task task0 = taskManager.getTaskById(1);
        Assertions.assertEquals(task0, task);
    }

    @Test
    void getSubtaskByIdMethodTest() {
        taskManager.addSubtaskObj(subtask);
        Subtask subtask0 = taskManager.getSubtaskById(1);
        Assertions.assertEquals(subtask0, subtask);
    }

    @Test
    void getEpicByIdMethodTest() {
        taskManager.addEpicObj(epic);
        Epic epic0 = taskManager.getEpicById(1);
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
    void deleteSubtaskByIdMethodTest() {
        taskManager.addSubtaskObj(subtask);
        taskManager.deleteSubtaskById(1);
        Assertions.assertEquals(taskManager.getSubtaskById(1), null);
    }

    @Test
    void deleteEpicByIdMethodTest() {
        taskManager.addEpicObj(epic);
        taskManager.deleteEpicById(1);
        Assertions.assertEquals(taskManager.getEpicById(1), null);
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
        epic.linkSubtaskToEpic(1);
        epic.linkSubtaskToEpic(2);
        taskManager.addSubtaskObj(subtask);
        taskManager.addSubtaskObj(subtask0);
        taskManager.updateEpicStatus(epic);
        Assertions.assertEquals(epic.getStatus(), Status.NEW);
    }

    @Test
    void updateEpicStatusWhenAllSubtasksDONE() {
        subtask.setStatus(Status.DONE);
        subtask0.setStatus(Status.DONE);
        epic.linkSubtaskToEpic(1);
        epic.linkSubtaskToEpic(2);
        taskManager.addSubtaskObj(subtask);
        taskManager.addSubtaskObj(subtask0);
        taskManager.updateEpicStatus(epic);
        Assertions.assertEquals(epic.getStatus(), Status.DONE);
    }

    @Test
    void updateEpicStatusWhenAllSubtasksIN_PROGRESS() {
        subtask.setStatus(Status.IN_PROGRESS);
        subtask0.setStatus(Status.DONE);
        epic.linkSubtaskToEpic(1);
        epic.linkSubtaskToEpic(2);
        taskManager.addSubtaskObj(subtask);
        taskManager.addSubtaskObj(subtask0);
        taskManager.updateEpicStatus(epic);
        Assertions.assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }
}