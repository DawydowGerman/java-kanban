package ru.practicum.kanban.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Epic extends Task {
    private Map<Integer, Subtask> subtasksMap = new HashMap<>();

    public Epic() {
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(int durationLength,int year, int month, int day, int hour, int minute) {
        super(durationLength, year, month, day, hour, minute);
    }

    public void linkSubtaskToEpic(Subtask subtask) {
        if (subtask == null || subtask.getIdNum() == null) return;
        if (this.getIdNum().equals(subtask.getIdNum())) return;
        subtasksMap.put(subtask.getIdNum(), subtask);
        setDurationStartTimeEndTime();
    }

    public List<Integer> getSubtasksId() {
        List<Integer> listOfSubtasksIds = new ArrayList<>(subtasksMap.keySet());
        return listOfSubtasksIds;
    }

    public void deleteSubtaskById(Integer subtaskId) {
        subtasksMap.remove(subtaskId);
        setDurationStartTimeEndTime();
    }

    public void cleanSubtaskIds() {
        subtasksMap.clear();
        setDurationStartTimeEndTime();
    }

    public void setDurationStartTimeEndTime() {
        List<Subtask> subsList = new ArrayList<Subtask>(subtasksMap.values());
        List<Duration> subsDurList = subsList.stream().map(Subtask::getDuration).collect(Collectors.toList());
        Duration duration = subsDurList.stream().reduce(Duration.ZERO, (t, d) -> t = t.plus(d));
        setDuration((int) duration.toMinutes());

        LocalDateTime startTime = subsList.stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
        setStartTime(startTime);

        LocalDateTime endTime = subsList.stream()
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
        setEndTime(endTime);
    }

    @Override
    public void setDuration(int durationLength) {
        super.setDuration(durationLength);
    }

    @Override
    public Duration getDuration() {
        return super.getDuration();
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        super.setStartTime(startTime);
    }

    @Override
    public LocalDateTime getStartTime() {
       return super.getStartTime();
    }

    @Override
    public void setEndTime(LocalDateTime endTime) {
        super.setEndTime(endTime);
    }

    @Override
    public LocalDateTime getEndTime() {
        return super.getEndTime();
    }
}