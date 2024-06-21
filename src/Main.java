package main.kanban1.java.src;

import main.kanban1.java.src.Interfaces.TaskManager;
import main.kanban1.java.src.manager.InMemoryTaskManager;
import main.kanban1.java.src.tasks.Epic;
import main.kanban1.java.src.tasks.Subtask;
import main.kanban1.java.src.tasks.Task;
import main.kanban1.java.src.manager.InMemoryHistoryManager;
import main.kanban1.java.src.utilClass.Managers;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
 /*       Managers managers = new Managers();
        TaskManager taskManager = managers.getDefault();

        Task task = new Task("task","to do something");
        taskManager.addTaskObj(task);

        taskManager.deleteTaskById(1);

        System.out.println(taskManager.getTaskById(1));

  */


       Task task = new Task("task","to do something");
       Task task0 = new Task("task","to do something");

        System.out.println(task.hashCode());
        System.out.println(task0.hashCode());

    }
}
