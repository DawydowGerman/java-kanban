package main.Kanban;

import main.Kanban.manager.Manager;
import main.Kanban.tasks.Epic;
import main.Kanban.tasks.Subtask;
import main.Kanban.tasks.Task;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
// test
        Task task0 = new Task("Flat's cleaning","to clean the whole flat");
        Task task1 = new Task("Garage's cleaning","to clean the whole garage");
        Task task2 = new Task("Food buying","to buy the food for next three days");

        manager.addTaskObj(task0);
        manager.addTaskObj(task1);
        manager.addTaskObj(task2);

        Epic epic0 = new Epic("A new bank card","to open a new bank card");
        Subtask subtask0OfEpic0 = new Subtask("Find bank office","to find the nearest bank's office");
        Subtask subtask1OfEpic0 = new Subtask("The conditions","to check the conditions for a cars opening");
        Subtask subtask2OfEpic0 = new Subtask("Bank's attendance","to go to the office");

        Epic epic1 = new Epic("The next summer vacation","to organize the next summer vacation");
        Subtask subtask0OfEpic1 = new Subtask("A place to go","to choose a place to go");

        manager.addEpicObj(epic0);
        manager.addSubtaskObj(subtask0OfEpic0);
        manager.addSubtaskObj(subtask1OfEpic0);
        manager.addSubtaskObj(subtask2OfEpic0);
        manager.addEpicObj(epic1);
        manager.addSubtaskObj(subtask0OfEpic1);
    }
}
