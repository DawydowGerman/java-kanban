package main.kanban1.java.src.tasks;

import main.kanban1.java.src.tasks.Task;

public class Subtask extends Task {
    private int epicId;

    public Subtask() {
    }

    public Subtask(String name, String description) {
        super(name, description);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        if(this.getIdNum() == epicId) return;
        this.epicId = epicId;
    }
}
