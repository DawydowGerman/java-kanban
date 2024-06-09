package main.Kanban.tasks;

import main.Kanban.tasks.Task;

import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> subtasksIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public void linkSubtaskToEpic(Integer id) {
        subtasksIds.add(id);
    }

     public ArrayList<Integer> getSubtasksId() {
         return subtasksIds;
    }

    public void cleanSubtaskIds () {
        subtasksIds.clear();
    }
}