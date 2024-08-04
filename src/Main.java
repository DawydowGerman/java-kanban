package main.kanban1.java.src;

import main.kanban1.java.src.Interfaces.TaskManager;
import main.kanban1.java.src.tasks.Epic;
import main.kanban1.java.src.tasks.Subtask;
import main.kanban1.java.src.util.Managers;

public class Main {
    public static void main(String[] args) {
        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefault();

        Subtask subtask = new Subtask("task","to do something");
        Subtask subtask0 = new Subtask("task","to do something other");

        Epic epic = new Epic("task","to do something");

        subtask.setIdNum(1);
        subtask0.setIdNum(2);

        epic.linkSubtaskToEpic(1);
        epic.linkSubtaskToEpic(2);

        taskManager.addSubtaskObj(subtask);
        taskManager.addSubtaskObj(subtask0);

        taskManager.updateEpicStatus(epic); // here is the issue


    }
}
