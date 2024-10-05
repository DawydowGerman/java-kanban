package main.kanban1.java.src.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasksListOfThisEpic = new HashMap<>();
    private Duration duration = Duration.ofMinutes(0);
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Epic() {
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    public void linkSubtaskToEpic(Subtask subtask) {
        if (subtask == null) return;
        if (this.getIdNum() == subtask.getIdNum()) return;
        subtasksListOfThisEpic.put(subtask.getIdNum(), subtask);
    }

    public ArrayList<Integer> getSubtasksId() {
        ArrayList<Integer> listOfSubtasksIds = new ArrayList<>(subtasksListOfThisEpic.keySet());
        return listOfSubtasksIds;
    }

    public void deleteSubtaskId(Integer subtaskId) {
        subtasksListOfThisEpic.remove(subtaskId);
    }

    public void cleanSubtaskIds() {
        subtasksListOfThisEpic.clear();
    }

    public void setDuration() {
        List<Subtask> subsList = new ArrayList<Subtask>(subtasksListOfThisEpic.values());
        List<Duration> subsDurList = subsList.stream().map(Subtask::getDuration).collect(Collectors.toList());
        this.duration = subsDurList.stream().reduce(Duration.ZERO, (t, d) -> t = t.plus(d));
    }

    public void setDurationDirectly(int durationLength) {
        this.duration = Duration.ofMinutes(durationLength);
    }

    @Override
    public Duration getDuration() {
        return this.duration;
    }

    public void setStartTime() {
        ArrayList<Integer> listOfSubtasksIds = this.getSubtasksId();
        for (int i = 0; i < listOfSubtasksIds.size() - 1; i++) {
            Subtask subtask = subtasksListOfThisEpic.get(listOfSubtasksIds.get(i));
            Subtask nextSubtask = subtasksListOfThisEpic.get(listOfSubtasksIds.get(i + 1));
            if (subtask.getStartTime().isBefore(nextSubtask.getStartTime())) {
                this.startTime = subtask.getStartTime();
            } else {
                this.startTime = nextSubtask.getStartTime();
            }
        }
    }

    public void setStartTimeDirectly(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        ArrayList<Integer> listOfSubtasksIds = this.getSubtasksId();
        for (int i = 0; i < listOfSubtasksIds.size() - 1; i++) {
            Subtask subtask = subtasksListOfThisEpic.get(listOfSubtasksIds.get(i));
            Subtask nextSubtask = subtasksListOfThisEpic.get(listOfSubtasksIds.get(i + 1));
            if (subtask.getEndTime().isAfter(nextSubtask.getEndTime())) {
                this.endTime = subtask.getEndTime();
            } else {
                this.endTime = nextSubtask.getEndTime();
            }
        }
        return endTime;
    }

    public void setEndTimeDirectly(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
