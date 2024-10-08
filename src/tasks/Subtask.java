package main.kanban1.java.src.tasks;

public class Subtask extends Task {
    private int epicId;

    public Subtask() {
    }

    public Subtask(String name, String description) {
        super(name, description);
    }

    public Subtask(int durationLength,int year, int month, int day, int hour, int minute) {
        super(durationLength, year, month, day, hour, minute);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        if (this.getIdNum() == epicId) return;
        this.epicId = epicId;
    }
}
