package main.kanban1.java.src.tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksIds = new ArrayList<>();

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

    public void deleteSubtaskId(Integer subtaskId) {
        for (int i = 0; i < subtasksIds.size(); i++) {
            if (subtaskId == subtasksIds.get(i)) {
                subtasksIds.remove(i);
            }
        }
    }

    public void cleanSubtaskIds() {
        subtasksIds.clear();
    }
}
