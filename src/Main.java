package main.kanban1.java.src;

import main.kanban1.java.src.Interfaces.TaskManager;
import main.kanban1.java.src.tasks.Epic;
import main.kanban1.java.src.tasks.Subtask;
import main.kanban1.java.src.tasks.Task;
import main.kanban1.java.src.util.Managers;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Task task = new Task(125,2014, 3, 2, 5, 12);
        Task task0 = new Task(150,2024, 3, 2, 5, 12);
        Subtask subtask = new Subtask(134,2020, 3, 2, 5, 12);
        Epic epic = new Epic(134,2021, 3, 2, 5, 12);

        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefault();

        taskManager.addTaskObj(task);
        taskManager.addTaskObj(task0);
        taskManager.addSubtaskObj(subtask);
        taskManager.addEpicObj(epic);

        taskManager.deleteAllEpics();

        ArrayList<Task> treeSetList = taskManager.getPrioritizedTasks();

        for(Task taskL : treeSetList) {
            System.out.println("duration: " + taskL.getDuration().toMinutes());
        }
        System.out.println("treeSetList size is: " + treeSetList.size());


    }
}
