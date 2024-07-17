package main.kanban1.java.src;

import main.kanban1.java.src.tasks.Node;
import main.kanban1.java.src.tasks.Subtask;
import main.kanban1.java.src.tasks.Task;
import main.kanban1.java.src.manager.InMemoryHistoryManager;

public class Main {
    public static void main(String[] args) {
        Task task = new Task("task","to do something one");
        Task task0 = new Task("task","to do something other");
        Subtask subtask = new Subtask("subtask","to do something as subtask");

        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        historyManager.linkLast(task);
        System.out.println("check added or not " + historyManager.mapHistory.size());

        historyManager.linkLast(task0);
        System.out.println("check added or not " + historyManager.mapHistory.size());

        historyManager.linkLast(subtask);

        for(Node node : historyManager.mapHistory.values()) {
            Task taskLoop = (Task) node.getTask();
            System.out.println(taskLoop.getDescription());
        }



    }
}
