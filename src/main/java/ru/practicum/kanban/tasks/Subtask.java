package ru.practicum.kanban.tasks;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask() {
    }

    public Subtask(String name, String description) {
        super(name, description);
    }

    public Subtask(int durationLength,int year, int month, int day, int hour, int minute) {
        super(durationLength, year, month, day, hour, minute);
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        if (this.getIdNum().equals(epicId)) return;
        this.epicId = epicId;
    }
}