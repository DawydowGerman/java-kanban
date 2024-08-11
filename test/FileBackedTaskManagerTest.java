package main.kanban1.java.test;

import main.kanban1.java.src.Interfaces.TaskManager;
import main.kanban1.java.src.manager.FileBackedTaskManager;
import main.kanban1.java.src.status.Status;
import main.kanban1.java.src.tasks.Task;
import main.kanban1.java.src.util.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;

public class FileBackedTaskManagerTest {
    File file;
    Task task0;
    Task task1;
    FileBackedTaskManager fileBackedTaskManager;
    Managers managers;
    TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        try {
            file = File.createTempFile("tmp", ".txt", new File("/home/german/"));
            System.out.println("File path: " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        fileBackedTaskManager = new FileBackedTaskManager(file);
        managers = new Managers();
        taskManager = managers.getDefault();

        task0 = new Task("Task1", "Description of the task1");
        task1 = new Task("Task2", "Description of the task2");

   //     file.deleteOnExit();
    }

    @Test
    void saveEmptyFileTest() {
        fileBackedTaskManager.save();
        ArrayList<Task> taskList = fileBackedTaskManager.getTasks();
        Assertions.assertEquals(taskList.size(), 0);
    }

    @Test
    void loadEmptyFileTest() {
        fileBackedTaskManager.loadFromFile(file);
        ArrayList<Task> taskList = fileBackedTaskManager.getTasks();
        Assertions.assertEquals(taskList.size(), 0);
    }

    @Test
    void saveFewTasks() {
        fileBackedTaskManager.addTaskObj(task0);
        fileBackedTaskManager.addTaskObj(task0);
        ArrayList<Task> taskList = fileBackedTaskManager.getTasks();
        Assertions.assertEquals(taskList.size(), 2);
    }

    @Test
    void loadFewTasks() {
        fileBackedTaskManager.addTaskObj(task0);
        fileBackedTaskManager.addTaskObj(task1);
        FileBackedTaskManager fileBackedTaskManager1 = fileBackedTaskManager.loadFromFile(file);
        ArrayList<Task> taskList = fileBackedTaskManager1.taskManager.getTasks();
        Assertions.assertEquals(taskList.size(), 2);
    }

    @Test
    void toStringTest() {
        Task task2 = new Task("Task1", "Description of the task1");
        Task task3 = new Task("Task1", "Description of the task1");
        task2.setStatus(Status.DONE);
        task2.setIdNum(113);
        task3.setStatus(Status.DONE);
        task3.setIdNum(113);
        String strTask = fileBackedTaskManager.toString(task2) + "\n";
        String strTask0 = fileBackedTaskManager.toString(task3) + "\n";
        Assertions.assertEquals(strTask, strTask0);
    }

    @Test
    void fromStringTest() {
        Task task2 = new Task("Task1", "Description of the task1");
        Task task3 = new Task("Task1", "Description of the task1");
        task2.setStatus(Status.DONE);
        task2.setIdNum(113);
        task3.setStatus(Status.DONE);
        task3.setIdNum(113);
        String strTask = fileBackedTaskManager.toString(task2) + "\n";
        String strTask0 = fileBackedTaskManager.toString(task3) + "\n";

        Task task4 = fileBackedTaskManager.fromString(strTask);
        Task task5 = fileBackedTaskManager.fromString(strTask0);
        Assertions.assertEquals(task4, task5);
    }



}
