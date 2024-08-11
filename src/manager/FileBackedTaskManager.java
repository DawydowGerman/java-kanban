package main.kanban1.java.src.manager;

import main.kanban1.java.src.Interfaces.TaskManager;
import main.kanban1.java.src.exceptions.ManagerSaveException;
import main.kanban1.java.src.status.Status;
import main.kanban1.java.src.tasks.Epic;
import main.kanban1.java.src.tasks.Subtask;
import main.kanban1.java.src.tasks.Task;
import main.kanban1.java.src.util.Managers;
import java.io.*;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static File file;

    public static Managers managers = new Managers();
    public static TaskManager taskManager = managers.getDefault();

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public void save() {
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,epic\n");

            ArrayList<Task> taskList = super.getTasks();
            for(Task task : taskList) {
                fileWriter.write(toString(task) + "\n");
            }

            ArrayList<Subtask> subtaskList = super.getSubtasks();
            for(Subtask subtask : subtaskList) {
                fileWriter.write(toString(subtask) + "\n");
            }

            ArrayList<Epic> epicsList = super.getEpics();
            for(Epic epic : epicsList) {
                fileWriter.write(toString(epic) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ManagerSaveException();
        }
    }

    public static String toString(Task task) {
        if (task == null) return null;

        String result = "";

        if (task instanceof Subtask) {
            result = Integer.toString(task.getIdNum()) + ","
                    + task.getClass().getSimpleName().toUpperCase() + ","
                    + task.getName() + ","
                    + task.getStatus() + ","
                    + task.getDescription() + ","
                    + ((Subtask) task).getEpicId();
        } else {
            result = Integer.toString(task.getIdNum()) + ","
                    + task.getClass().getSimpleName().toUpperCase() + ","
                    + task.getName() + ","
                    + task.getStatus() + ","
                    + task.getDescription() + ",";
        }
        return result;
    }

    public static Task fromString(String value) {
        if (value == null) return null;

        int firstComma = value.indexOf(",");
        int secondComma = value.indexOf(",",firstComma + 1);
        int thirdComma =  value.indexOf(",", secondComma + 1);
        int fourthComma = value.indexOf(",", thirdComma + 1);
        int fifthComma = value.indexOf(",", fourthComma + 1);

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

        switch (taskType) {
            case "TASK":
                Task task = new Task(taskName, taskDesc);
                task.setIdNum(taskId);
                task.setStatus(taskStatus);
                return task;

            case "SUBTASK":
                Subtask subtask = new Subtask(taskName, taskDesc);
                String epicIdStr = value.substring(value.indexOf(",",fifthComma) + 1, value.length());
                int epicId = Integer.parseInt(epicIdStr);
                subtask.setIdNum(taskId);
                subtask.setStatus(taskStatus);
                subtask.setEpicId(epicId);
                return subtask;

            case "EPIC":
                Epic epic = new Epic(taskName, taskDesc);
                epic.setIdNum(taskId);
                epic.setStatus(taskStatus);
                return epic;
        }
        return null;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        if (file == null) return null;

        try(Reader fileReader = new FileReader(file)) {
            BufferedReader br = new BufferedReader(fileReader);

        while(br.ready()) {
            String line = br.readLine();

            int firstComma = line.indexOf(",");
            int secondComma = line.indexOf(",",firstComma + 1);

            String taskType = line.substring(line.indexOf(",",firstComma) + 1,
            line.indexOf(",",secondComma));

            switch (taskType) {
                case "TASK":
                    Task task = fromString(line);
                    taskManager.addTaskObj(task);
                    break;

                    case "SUBTASK":
                    Subtask subtask = (Subtask) fromString(line);
                    taskManager.addSubtaskObj(subtask);
                    break;

                    case "EPIC":
                    Epic epic = (Epic) fromString(line);
                    taskManager.addEpicObj(epic);
                    break;
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
