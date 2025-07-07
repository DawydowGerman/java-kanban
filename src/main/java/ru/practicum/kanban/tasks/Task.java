package ru.practicum.kanban.tasks;

import ru.practicum.kanban.status.Status;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private Integer idNum;
    private Status status = Status.NEW;
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Task() {
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(int durationLength, int year, int month, int day, int hour, int minute) {
        this.startTime = LocalDateTime.of(year, month, day, hour, minute);
        this.duration = Duration.ofMinutes(durationLength);
        setEndTime(startTime.plus(duration));
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getIdNum() {
        return idNum;
    }

    public Status getStatus() {
        return status;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setName(String name) {
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

    public void setDuration(int durationLength) {
        this.duration = Duration.ofMinutes(durationLength);
    }

    public void setStartTime(int year, int month, int day, int hour, int minute) {
        this.startTime = LocalDateTime.of(year, month, day, hour, minute);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return idNum.equals(task.idNum) &&
                Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                (status == task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, idNum, status);
    }
}