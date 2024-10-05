package main.kanban1.java.src.manager;

import main.kanban1.java.src.Interfaces.TaskManager;
import main.kanban1.java.src.exceptions.ManagerSaveException;
import main.kanban1.java.src.status.Status;
import main.kanban1.java.src.tasks.Epic;
import main.kanban1.java.src.tasks.Subtask;
import main.kanban1.java.src.tasks.Task;
import main.kanban1.java.src.util.Managers;

import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static File file;

    public static Managers managers = new Managers();
    public static TaskManager taskManager = managers.getDefault();

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public void save() {
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,epic,duration,startTime,endTime\n");

            ArrayList<Task> taskList = super.getTasks();
            taskList.stream()
            .map(p -> toString(p) + "\n") // Stream<String>
                    .forEach(s -> {
                        try {
                            fileWriter.write(s);
                        } catch (Exception e) {
                            // internally catched
                        }
                    });

            ArrayList<Subtask> subtaskList = super.getSubtasks();
            subtaskList.stream()
                    .map(p -> toString(p) + "\n") // Stream<String>
                    .forEach(s -> {
                        try {
                            fileWriter.write(s);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    });

            ArrayList<Epic> epicsList = super.getEpics();
            epicsList.stream()
                    .map(p -> toString(p) + "\n") // Stream<String>
                    .forEach(s -> {
                        try {
                            fileWriter.write(s);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    });
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    public static String toString(Task task) {
        if (task == null) return null;
        String result = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
        if (task instanceof Subtask) {
            String startTime = task.getStartTime().format(formatter);
            String endTime = task.getEndTime().format(formatter);
            result = Integer.toString(task.getIdNum()) + ","
                    + task.getClass().getSimpleName().toUpperCase() + ","
                    + task.getName() + ","
                    + task.getStatus() + ","
                    + task.getDescription() + ","
                    + ((Subtask) task).getEpicId() + ","
                    + task.getDuration().toMinutes() + ","
                    + startTime + ","
                    + endTime + ",";
        } else {
            String startTime = task.getStartTime().format(formatter);
            String endTime = task.getEndTime().format(formatter);
            result = Integer.toString(task.getIdNum()) + ","
                    + task.getClass().getSimpleName().toUpperCase() + ","
                    + task.getName() + ","
                    + task.getStatus() + ","
                    + task.getDescription() + ","
                    + task.getDuration().toMinutes() + ","
                    + startTime + ","
                    + endTime + ",";
        }
        return result;
    }

    public static Task fromString(String value) {
        if (value == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");

        int firstComma = value.indexOf(",");
        int secondComma = value.indexOf(",",firstComma + 1);
        int thirdComma =  value.indexOf(",", secondComma + 1);
        int fourthComma = value.indexOf(",", thirdComma + 1);
        int fifthComma = value.indexOf(",", fourthComma + 1);
        int sixthComma = value.indexOf(",", fifthComma + 1);
        int seventhComma = value.indexOf(",", sixthComma + 1);
        int eighthComma = value.indexOf(",", seventhComma + 1);
        int ninthComma = value.indexOf(",", eighthComma + 1);
        int tenthComma = value.indexOf(",", ninthComma + 1);
        int eleventhComma = value.indexOf(",", tenthComma + 1);

        String taskIdStr = value.substring(0, firstComma);
        int taskId = Integer.parseInt(taskIdStr);

        String taskType = value.substring(value.indexOf(",",firstComma) + 1,
                value.indexOf(",",secondComma));

        String taskName = value.substring(value.indexOf(",",secondComma) + 1,
                value.indexOf(",",thirdComma));

        String status = value.substring(value.indexOf(",",thirdComma) + 1,
                value.indexOf(",",fourthComma));
        Status taskStatus = Status.valueOf(status);

        String taskDesc = value.substring(value.indexOf(",",fourthComma) + 1,
                value.indexOf(",",fifthComma));

        String epicIdStr;
        int epicId = 0;
        String taskDurationStr;
        int taskDuration;
        String startTimeStr;
        LocalDateTime startTime;
        String endTimeStr;
        LocalDateTime endTime;

        if (taskType.equals("SUBTASK")) {
            epicIdStr = value.substring(value.indexOf(",", fifthComma) + 1,
                    value.indexOf(",", sixthComma));
            epicId = Integer.parseInt(epicIdStr);

            taskDurationStr = value.substring(value.indexOf(",", sixthComma) + 1,
                    value.indexOf(",", seventhComma));
            taskDuration = Integer.parseInt(taskDurationStr);

            startTimeStr = value.substring(value.indexOf(",", seventhComma) + 1,
                    value.indexOf(",", ninthComma));
            startTime = LocalDateTime.parse(startTimeStr, formatter);

            endTimeStr = value.substring(value.indexOf(",", ninthComma) + 1,
                    value.length() - 1);
            endTime = LocalDateTime.parse(endTimeStr, formatter);
        } else {
            taskDurationStr = value.substring(value.indexOf(",", fifthComma) + 1,
                    value.indexOf(",", sixthComma));
            taskDuration = Integer.parseInt(taskDurationStr);

            startTimeStr = value.substring(value.indexOf(",", sixthComma) + 1,
                    value.indexOf(",", eighthComma));
            startTime = LocalDateTime.parse(startTimeStr, formatter);

            endTimeStr = value.substring(value.indexOf(",", eighthComma) + 1,
                    value.length() - 1);
            endTime = LocalDateTime.parse(endTimeStr, formatter);
        }

        switch (taskType) {
            case "TASK":
                Task task = new Task(taskName, taskDesc);
                task.setIdNum(taskId);
                task.setStatus(taskStatus);
                task.setDuration(taskDuration);
                task.setStartTime(startTime);
                task.getEndTime();
                return task;

            case "SUBTASK":
                Subtask subtask = new Subtask(taskName, taskDesc);
                subtask.setIdNum(taskId);
                subtask.setStatus(taskStatus);
                subtask.setEpicId(epicId);
                subtask.setDuration(taskDuration);
                subtask.setStartTime(startTime);
                subtask.getEndTime();
                return subtask;

            case "EPIC":
                Epic epic = new Epic(taskName, taskDesc);
                epic.setIdNum(taskId);
                epic.setStatus(taskStatus);
                epic.setDurationDirectly(taskDuration);
                epic.setStartTimeDirectly(startTime);
                epic.setEndTimeDirectly(endTime);
                return epic;
        }
        return null;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        if (file == null) return null;

        List<String> list = null;
        try (Stream<String> lines = Files.lines(file.toPath())) {
            list = lines.collect(Collectors.toList());
            } catch (IOException e) {
            throw new ManagerSaveException();
        }

        list.stream()
            .skip(1)
            .forEach(i -> {
                if (i.substring(i.indexOf(",",i.indexOf(",")) + 1,
                    i.indexOf(",",i.indexOf(",",i.indexOf(",") + 1))).equals("TASK")) {
                    Task task = fromString(i);
                    taskManager.addTaskObj(task);
                } else if (i.substring(i.indexOf(",",i.indexOf(",")) + 1,
                     i.indexOf(",",i.indexOf(",",i.indexOf(",") + 1))).equals("SUBTASK")) {
                    Subtask subtask = (Subtask) fromString(i);
                    taskManager.addSubtaskObj(subtask);
                } else if (i.substring(i.indexOf(",",i.indexOf(",")) + 1,
                    i.indexOf(",",i.indexOf(",",i.indexOf(",") + 1))).equals("EPIC")) {
                    Epic epic = (Epic) fromString(i);
                    taskManager.addEpicObj(epic);
                }});
        return new FileBackedTaskManager(file);
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void addTaskObj(Task task) {
        super.addTaskObj(task);
        save();
    }

    @Override
    public void addSubtaskObj(Subtask subtask) {
        super.addSubtaskObj(subtask);
        save();
    }

    @Override
    public void addEpicObj(Epic epic) {
        super.addEpicObj(epic);
        save();
    }

    @Override
    public void updTask(Task task) {
        super.updTask(task);
        save();
    }

    @Override
    public void updSubtask(Subtask subtask) {
        super.updSubtask(subtask);
        save();
    }

    @Override
    public void updEpic(Epic epic) {
        super.updEpic(epic);
        save();
    }

    @Override
    public void deleteTaskById(Integer id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(Integer id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        super.updateEpicStatus(epic);
        save();
    }

    @Override
    public ArrayList<Task> getTasks() {
        return super.getTasks();
    }
}
