package ru.practicum.kanban;

import ru.practicum.kanban.exceptions.OvelapException;
import ru.practicum.kanban.interfaces.HistoryManager;
import ru.practicum.kanban.interfaces.TaskManager;
import ru.practicum.kanban.manager.FileBackedTaskManager;
import ru.practicum.kanban.status.Status;
import ru.practicum.kanban.tasks.Epic;
import ru.practicum.kanban.tasks.Subtask;
import ru.practicum.kanban.tasks.Task;
import ru.practicum.kanban.util.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

class TaskManagerTest {
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
    Subtask subtask7;
    Subtask subtask8;
    Epic epic;
    Epic epic0;
    Epic epic1;
    Epic epic2;
    Epic epic3;
    Epic epic4;
    Managers managers;
    TaskManager taskManager;
    TaskManager taskManager0;
    HistoryManager historyManager;

    File file;
    Task taskFilebacked0;
    Task taskFilebacked1;
    FileBackedTaskManager fileBackedTaskManager;

    @BeforeEach
    public void beforeEach() {
        task = new Task("task","to do something");
        task.setStartTime(2024, 3, 15, 16, 32);
        task.setDuration(10080);
        task.setEndTime(task.getStartTime().plus(task.getDuration()));
        task0 = new Task("task","to do something");
        task0.setStartTime(2024, 3, 22, 16, 32);
        task0.setDuration(10);
        task0.setEndTime(task0.getStartTime().plus(task0.getDuration()));
        task1 = new Task("task","to do something other");
        task1.setStartTime(2000, 3, 15, 16, 32);
        task1.setDuration(10080);
        task1.setEndTime(task1.getStartTime().plus(task1.getDuration()));
        taskforsecondtest = new Task("task","to do something");
        taskforsecondtest.setStartTime(2024, 3, 15, 16, 32);
        taskforsecondtest.setDuration(60);
        taskforsecondtest.setIdNum(13);
        subtask = new Subtask("task","to do something");
        subtask.setStartTime(2024, 3, 15, 16, 32);
        subtask.setDuration(100);
        subtask.setEndTime(subtask.getStartTime().plus(subtask.getDuration()));
        subtask0 = new Subtask("task","to do something other");
        subtask0.setStartTime(2014, 3, 15, 16, 32);
        subtask0.setDuration(5);
        subtask0.setEndTime(subtask0.getStartTime().plus(subtask0.getDuration()));
        subtask1 = new Subtask("task","to do something as subtask1");
        subtask1.setStartTime(2024, 3, 15, 16, 32);
        subtask1.setDuration(10);
        subtask1.setEndTime(subtask1.getStartTime().plus(subtask1.getDuration()));
        subtask2 = new Subtask("task","to do something other as subtask2");
        subtask2.setStartTime(2024, 4, 15, 16, 32);
        subtask2.setDuration(10080);
        subtask2.setEndTime(subtask2.getStartTime().plus(subtask2.getDuration()));
        subtask3 = new Subtask("task","to do something as subtask3");
        subtask4 = new Subtask("task","to do something other as subtask4");
        subtask5 = new Subtask("task","to do something as subtask5");
        subtask5.setStartTime(2024, 3, 15, 16, 32);
        subtask5.setDuration(10080);
        subtask5.setEndTime(subtask5.getStartTime().plus(subtask5.getDuration()));
        subtask6 = new Subtask("task","to do something other as subtask6");
        subtask6.setStartTime(2024, 3, 22, 16, 33);
        subtask6.setDuration(10080);
        subtask6.setEndTime(subtask6.getStartTime().plus(subtask6.getDuration()));
        subtask7 = new Subtask("task","to do something as subtask7");
        subtask8 = new Subtask("task","to do something other as subtask8");
        epic = new Epic("task","to do something");
        epic0 = new Epic("task","to do something other");
        epic1 = new Epic("task","to do something other");
        epic2 = new Epic("task","to do something other");
        epic3 = new Epic("task","to do something other");
        epic4 = new Epic("task","to do something other");
        managers = new Managers();
        taskManager = managers.getDefault();
        taskManager0 = managers.getDefault();
        historyManager = managers.getDefaultHistory();

        try {
            file = File.createTempFile("tmp", ".txt");
            System.out.println("File path: " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        fileBackedTaskManager = new FileBackedTaskManager(file);
        taskFilebacked0 = new Task("Task0", "Description of the task0");
        taskFilebacked1 = new Task("Task1", "Description of the task1");
        file.deleteOnExit();
    }

    @Test
    void tasksWithSettedAndGeneratedIdsDontConflict() {
        taskManager.addTaskObj(task);
        task0.setStartTime(2020, 3, 22, 16, 32);
        task0.setDuration(10);
        task0.setEndTime(task0.getStartTime().plus(task0.getDuration()));
        taskManager.addTaskObj(task0);
        task0.setIdNum(1);
        List<Task> list = taskManager.getTasks();
        Assertions.assertEquals(list.size(), 2);
    }

    @Test
    void immutabilityOfTasksFieldsAfterAdditionToManager() {
        taskManager.addTaskObj(taskforsecondtest);
        Task task = taskforsecondtest;
        Assertions.assertEquals(taskforsecondtest, task);
    }

    @Test
    void getTasksMethodTest() {
        taskManager.addTaskObj(task);
        List<Task> list = taskManager.getTasks();
        Assertions.assertEquals(list.size(), 1);
    }

    @Test
    void getSubtasksMethodTest() {
        taskManager.addSubtaskObj(subtask);
        List<Subtask> list = taskManager.getSubtasks();
        Assertions.assertEquals(list.size(), 1);
    }

    @Test
    void getEpicsMethodTest() {
        taskManager.addEpicObj(epic);
        List<Epic> list = taskManager.getEpics();
        Assertions.assertEquals(list.size(), 1);
    }

    @Test
    void deleteAllTasksMethodTest() {
        taskManager.addTaskObj(task);
        taskManager.deleteAllTasks();
        List<Task> list = taskManager.getTasks();
        Assertions.assertEquals(list.size(), 0);
    }

    @Test
    void deleteAllSubtasksMethodTest() {
        taskManager.addSubtaskObj(subtask);
        taskManager.deleteAllSubtasks();
        List<Subtask> list = taskManager.getSubtasks();
        Assertions.assertEquals(list.size(), 0);
    }

    @Test
    void deleteAllEpicsMethodTest() {
        taskManager.addEpicObj(epic);
        taskManager.deleteAllEpics();
        List<Epic> list = taskManager.getEpics();
        Assertions.assertEquals(list.size(), 0);
    }

    @Test
    void getTaskByIdMethodTest() {
        taskManager.addTaskObj(task);
        Task task0 = taskManager.getTaskById(task.getIdNum());
        Assertions.assertEquals(task0, task);
    }

     @Test
    void getSubtaskByIdMethodTest() {
        taskManager.addSubtaskObj(subtask);
        Subtask subtask0 = taskManager.getSubtaskById(subtask.getIdNum());
        Assertions.assertEquals(subtask0, subtask);
    }

    @Test
    void getEpicByIdMethodTest() {
        taskManager.addEpicObj(epic);
        Epic epic0 = taskManager.getEpicById(epic.getIdNum());
        Assertions.assertEquals(epic0, epic);
    }

    @Test
    void addTaskObjMethodTest() {
        taskManager.addTaskObj(task);
        List<Task> list = taskManager.getTasks();
        Assertions.assertEquals(list.size(), 1);
    }

    @Test
    void addTaskObjMethodExceptionTest() {
        OvelapException thrown = Assertions.assertThrows(
                OvelapException.class, () -> {
            taskManager.addTaskObj(task0);
            taskManager.addTaskObj(task);
        }, "Добавляемая задача пересекается с другой");

        Assertions.assertTrue(thrown.getMessage().contains("Добавляемая задача пересекается с другой"));
    }

    @Test
    void addSubtaskObjMethodTest() {
        taskManager.addSubtaskObj(subtask);
        List<Subtask> list = taskManager.getSubtasks();
        Assertions.assertEquals(list.size(), 1);
    }

    @Test
    void addEpicObjMethodTest() {
        taskManager.addEpicObj(epic);
        List<Epic> list = taskManager.getEpics();
        Assertions.assertEquals(list.size(), 1);
    }

    @Test
    void updTaskMethodTest() {
        taskManager.addTaskObj(task);
        taskManager.addTaskObj(task1);
        task1.setName("Other Name");
        taskManager.updTask(task1);
        Assertions.assertEquals(task1.getName(), "Other Name");
    }

    @Test
    void updSubtaskMethodTest() {
        taskManager.addSubtaskObj(subtask);
        taskManager.addSubtaskObj(subtask0);
        subtask0.setName("Another Name");
        taskManager.updSubtask(subtask0);
        Assertions.assertEquals(subtask0.getName(), "Another Name");
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
        epic.linkSubtaskToEpic(subtask);
        taskManager.getEpicById(epic.getIdNum());
        taskManager.deleteEpicById(1);
        List<Task> list = historyManager.getHistory();
        Assertions.assertEquals(list.size(), 0);
    }

    @Test
    void getAllSubtasksOfOneEpicMethodTest() {
        epic1.setIdNum(12);
        taskManager.addSubtaskObj(subtask1);
        taskManager.addSubtaskObj(subtask2);
        epic1.linkSubtaskToEpic(subtask1);
        epic1.linkSubtaskToEpic(subtask2);
        List<Subtask> list = taskManager.getAllSubtasksOfOneEpic(epic1);
        Assertions.assertEquals(list.size(), 2);
    }

    @Test
    void updateEpicStatusWhenAllSubtasksNEW() {
        taskManager0.addSubtaskObj(subtask1);
        taskManager0.addSubtaskObj(subtask2);
        taskManager0.addEpicObj(epic1);
        epic1.linkSubtaskToEpic(subtask1);
        epic1.linkSubtaskToEpic(subtask2);
        taskManager0.updateEpicStatus(epic1);
        Assertions.assertEquals(epic1.getStatus(), Status.NEW);
    }

    @Test
    void updateEpicStatusWhenAllSubtasksDONE() {
        taskManager0.addSubtaskObj(subtask1);
        taskManager0.addSubtaskObj(subtask2);
        taskManager0.addEpicObj(epic2);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        epic2.linkSubtaskToEpic(subtask1);
        epic2.linkSubtaskToEpic(subtask2);
        taskManager0.updateEpicStatus(epic2);
        Assertions.assertEquals(epic2.getStatus(), Status.DONE);
    }

    @Test
    void updateEpicStatusWhenSomeSubtasksNEWAndDONE() {
        taskManager0.addSubtaskObj(subtask1);
        taskManager0.addSubtaskObj(subtask2);
        taskManager0.addEpicObj(epic3);
        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.DONE);
        epic3.linkSubtaskToEpic(subtask1);
        epic3.linkSubtaskToEpic(subtask2);
        taskManager0.updateEpicStatus(epic3);
        Assertions.assertEquals(epic3.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    void updateEpicStatusWhenAllSubtasksINPROGRESS() {
        taskManager0.addSubtaskObj(subtask1);
        taskManager0.addSubtaskObj(subtask2);
        taskManager0.addEpicObj(epic3);
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);
        epic3.linkSubtaskToEpic(subtask1);
        epic3.linkSubtaskToEpic(subtask2);
        taskManager0.updateEpicStatus(epic3);
        Assertions.assertEquals(epic3.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    void getPrioritizedTasksTest() {
        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefault();
        Task task0 = new Task("Task0", "Description of the task0");
        task0.setDuration(60);
        task0.setStartTime(2024, 10,15,16,32);
        task0.setEndTime(task0.getStartTime().plus(task0.getDuration()));
        Task task1 = new Subtask("Task1", "Description of the task1");
        task1.setDuration(61);
        task1.setStartTime(2024, 3,16,1,34);
        task1.setEndTime(task1.getStartTime().plus(task1.getDuration()));
        Task task2 = new Task("Task2", "Description of the task2");
        task2.setDuration(99);
        task2.setStartTime(2024, 11,16,1,34);
        task2.setEndTime(task2.getStartTime().plus(task2.getDuration()));
        Task task3 = new Task("Task2", "Description of the task2");
        task3.setDuration(44);
        task3.setStartTime(2024, 5,16,1,34);
        task3.setEndTime(task3.getStartTime().plus(task3.getDuration()));
        taskManager.addTaskObj(task0);
        taskManager.addTaskObj(task1);
        taskManager.addTaskObj(task2);
        taskManager.addTaskObj(task3);
        ArrayList<Task> listOfTasks = taskManager.getPrioritizedTasks();
        Assertions.assertEquals("2024-03-16T01:34", listOfTasks.get(0).getStartTime().toString());
        Assertions.assertEquals("2024-05-16T01:34", listOfTasks.get(1).getStartTime().toString());
        Assertions.assertEquals("2024-10-15T16:32", listOfTasks.get(2).getStartTime().toString());
        Assertions.assertEquals("2024-11-16T01:34", listOfTasks.get(3).getStartTime().toString());
    }

    @Test
    void checkIntersectionsTestWhenDurationsDontOvelap() {
        boolean overlapVar = false;
        Assertions.assertEquals(overlapVar, taskManager.checkIntersections(subtask5, subtask6));
    }

    @Test
    void checkIntersectionsTestWhenDurationsOvelap() {
        boolean overlapVar = true;
        subtask6.setStartTime(2024, 3, 22, 16, 32);

        Assertions.assertEquals(overlapVar, taskManager.checkIntersections(subtask5, subtask6));
    }

    @Test
    void saveEmptyFileTest() {
        fileBackedTaskManager.save();
        List<Task> taskList = fileBackedTaskManager.getTasks();
        Assertions.assertEquals(taskList.size(), 0);
    }

    @Test
    void saveExceptionTest() {
        Assertions.assertDoesNotThrow(() -> {
            fileBackedTaskManager.save();});
    }

    @Test
    void loadEmptyFileTest() {
        fileBackedTaskManager.loadFromFile(file);
        List<Task> taskList = fileBackedTaskManager.getTasks();
        Assertions.assertEquals(taskList.size(), 0);
    }

    @Test
    void loadEmptyFileExceptionTest() {
        Assertions.assertDoesNotThrow(() -> {
            fileBackedTaskManager.loadFromFile(file);});
    }

    @Test
    void saveFewTasks() {
        taskFilebacked0.setIdNum(34);
        taskFilebacked1.setIdNum(28);
        taskFilebacked0.setDuration(60);
        taskFilebacked1.setDuration(60);
        taskFilebacked0.setStartTime(1987, 10,15,16,32);
        taskFilebacked1.setStartTime(1953, 3,16,1,34);
        taskFilebacked0.setEndTime(taskFilebacked0.getStartTime().plus(taskFilebacked0.getDuration()));
        taskFilebacked1.setEndTime(taskFilebacked1.getStartTime().plus(taskFilebacked1.getDuration()));
        fileBackedTaskManager.addTaskObj(taskFilebacked0);
        fileBackedTaskManager.addTaskObj(taskFilebacked1);
        List<Task> taskList = fileBackedTaskManager.getTasks();
        Assertions.assertEquals(taskList.size(), 2);
    }

    @Test
    void loadFewTasks() {
        taskFilebacked0.setIdNum(34);
        taskFilebacked1.setIdNum(28);
        taskFilebacked0.setDuration(60);
        taskFilebacked1.setDuration(60);
        taskFilebacked0.setStartTime(1987, 10,15,16,32);
        taskFilebacked1.setStartTime(1953, 3,16,1,34);
        taskFilebacked0.setEndTime(taskFilebacked0.getStartTime().plus(taskFilebacked0.getDuration()));
        taskFilebacked1.setEndTime(taskFilebacked1.getStartTime().plus(taskFilebacked1.getDuration()));
        fileBackedTaskManager.addTaskObj(taskFilebacked0);
        fileBackedTaskManager.addTaskObj(taskFilebacked1);
        List<Task> taskList = fileBackedTaskManager.getTasks();

        Assertions.assertEquals(taskList.size(), 2);
    }

    @Test
    void toStringTest() {
        Task task2 = new Task("Task1", "Description of the task1");
        Task task3 = new Task("Task1", "Description of the task1");
        task2.setStatus(Status.DONE);
        task2.setIdNum(113);
        task2.setDuration(60);
        task2.setStartTime(1987, 10,15,16,32);
        task2.setEndTime(task2.getStartTime().plus(task2.getDuration()));
        task3.setStatus(Status.DONE);
        task3.setIdNum(113);
        task3.setDuration(60);
        task3.setStartTime(1987, 10,15,16,32);
        task3.setEndTime(task3.getStartTime().plus(task3.getDuration()));
        String strTask = fileBackedTaskManager.toString(task2) + "\n";
        String strTask0 = fileBackedTaskManager.toString(task3) + "\n";
        Assertions.assertEquals(strTask, strTask0);
    }

    @Test
    void fromStringTest() {
        Task task2 = new Task("Task1", "Description");
        Task task3 = new Task("Task1", "Description");
        task2.setStatus(Status.DONE);
        task2.setIdNum(113);
        task2.setDuration(60);
        task2.setStartTime(1987, 10,15,16,32);
        task2.setEndTime(task2.getStartTime().plus(task2.getDuration()));
        task3.setStatus(Status.DONE);
        task3.setIdNum(113);
        task3.setDuration(60);
        task3.setStartTime(1987, 10,15,16,32);
        task3.setEndTime(task3.getStartTime().plus(task3.getDuration()));
        String strTask = fileBackedTaskManager.toString(task2);
        String strTask0 = fileBackedTaskManager.toString(task3);
        Task task4 = fileBackedTaskManager.fromString(strTask);
        Task task5 = fileBackedTaskManager.fromString(strTask0);
        Assertions.assertEquals(task4, task5);
    }

    @Test
    void epicExistenceForSubtask() {
        epic1.setIdNum(12);
        subtask1.setIdNum(3);
        subtask1.setStartTime(2022,3,15,12,35);
        subtask1.setDuration(60);
        subtask1.getEndTime();

        epic1.linkSubtaskToEpic(subtask1);
        subtask1.setEpicId(epic1.getIdNum());
        taskManager.addSubtaskObj(subtask1);
        taskManager.addEpicObj(epic1);
        Epic epicTest = taskManager.getEpicById(epic1.getIdNum());
        Assertions.assertEquals(epic1, epicTest);
    }
}