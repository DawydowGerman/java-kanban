package main.kanban1.java.src.tasks;

import main.kanban1.java.src.tasks.Task;
import java.util.List;
import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> subtasksIds = new ArrayList<>();

    public Epic() {
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    public void linkSubtaskToEpic(Integer id) {
        if (this.getIdNum() == id) return;
        subtasksIds.add(id);
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksIds;
    }

    public void cleanSubtaskIds() {
        subtasksIds.clear();
    }
}
