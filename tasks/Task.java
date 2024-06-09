package main.Kanban.tasks;

import main.Kanban.status.Status;

import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private int idNum;
    private Status status = Status.NEW;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName () {
        return name;
    }

    public String getDescription () {
        return description;
    }

    public int getIdNum () {
        return idNum;
    }

    public Status getStatus () {
        return status;
    }

    public void setName (String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIdNum(int idNum) {
        this.idNum = idNum;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return idNum == task.idNum &&
                Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                (status == task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, idNum, status);
    }
}
