package ru.practicum.kanban.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasksListOfThisEpic = new HashMap<>();

    public Epic() {
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(int durationLength,int year, int month, int day, int hour, int minute) {
        super(durationLength, year, month, day, hour, minute);
    }

    public void linkSubtaskToEpic(Subtask subtask) {
        if (subtask == null) return;
        if (this.getIdNum() == subtask.getIdNum()) return;
        subtasksListOfThisEpic.put(subtask.getIdNum(), subtask);
        setDurationStartTimeEndTime();
    }

    public ArrayList<Integer> getSubtasksId() {
        ArrayList<Integer> listOfSubtasksIds = new ArrayList<>(subtasksListOfThisEpic.keySet());
        return listOfSubtasksIds;
    }

    public void deleteSubtaskId(Integer subtaskId) {
        subtasksListOfThisEpic.remove(subtaskId);
        setDurationStartTimeEndTime();
    }

    public void cleanSubtaskIds() {
        subtasksListOfThisEpic.clear();
        setDurationStartTimeEndTime();
    }

    public void setDurationStartTimeEndTime() {
        List<Subtask> subsList = new ArrayList<Subtask>(subtasksListOfThisEpic.values());
        List<Duration> subsDurList = subsList.stream().map(Subtask::getDuration).collect(Collectors.toList());
        Duration duration = subsDurList.stream().reduce(Duration.ZERO, (t, d) -> t = t.plus(d));
        super.setDuration((int) duration.toMinutes());

        ArrayList<Integer> listOfSubtasksIds0 = this.getSubtasksId();
        LocalDateTime startTime = null;
        for (int i = 0; i < listOfSubtasksIds0.size() - 1; i++) {
            Subtask subtask = subtasksListOfThisEpic.get(listOfSubtasksIds0.get(i));
            Subtask nextSubtask = subtasksListOfThisEpic.get(listOfSubtasksIds0.get(i + 1));
            if (subtask.getStartTime().isBefore(nextSubtask.getStartTime())) {
                startTime = subtask.getStartTime();
            } else {
                startTime = nextSubtask.getStartTime();
            }
        }
        setStartTimeDirectly(startTime);

        ArrayList<Integer> listOfSubtasksIds1 = this.getSubtasksId();
        LocalDateTime endTime = null;
        for (int i = 0; i < listOfSubtasksIds1.size() - 1; i++) {
            Subtask subtask = subtasksListOfThisEpic.get(listOfSubtasksIds1.get(i));
            Subtask nextSubtask = subtasksListOfThisEpic.get(listOfSubtasksIds1.get(i + 1));
            if (subtask.getEndTime().isAfter(nextSubtask.getEndTime())) {
                endTime = subtask.getEndTime();
            } else {
                endTime = nextSubtask.getEndTime();
            }
        }
        setEndTimeDirectly(endTime);
    }

    public void setDurationDirectly(int durationLength) {
        super.setDuration(durationLength);
    }

    @Override
    public Duration getDuration() {
        return super.getDuration();
    }

    public void setStartTimeDirectly(LocalDateTime startTime) {
        super.setStartTime(startTime);
    }

    @Override
    public LocalDateTime getStartTime() {
       return super.getStartTime();
    }

    @Override
    public void setEndTimeDirectly(LocalDateTime endTime) {
        super.setEndTimeDirectly(endTime);
    }

    public LocalDateTime getEndTime() {
        return super.getEndTimeDirectly();
    }
}