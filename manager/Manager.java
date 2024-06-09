package main.Kanban.manager;

import main.Kanban.tasks.Epic;
import main.Kanban.status.Status;
import main.Kanban.tasks.Subtask;
import main.Kanban.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private static int idCounter = 1;
    private HashMap<Integer, Task> tasksList = new HashMap<>();
    private HashMap<Integer, Subtask> subtasksList = new HashMap<>();
    private HashMap<Integer, Epic> epicsList = new HashMap<>();

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasksList.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasksList.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epicsList.values());
    }

    public void deleteAllTasks() {
        tasksList.clear();
        System.out.println("Все задачи удалены.");
    }

    public void deleteAllSubtasks() {
        for (Epic epic : epicsList.values()) {
            this.updateEpicStatus(epic);
            epic.cleanSubtaskIds();
        }
        subtasksList.clear();
    }

    public void deleteAllEpics() {
        subtasksList.clear();
        epicsList.clear();
        System.out.println("Все эпики удалены.");
    }

    public Task getTaskById(Integer integer) {
        Task task = tasksList.get(integer);
        return task;
    }

    public Subtask getSubtaskById(Integer integer) {
        Subtask subtask = subtasksList.get(integer);
        return subtask;
    }

    public Epic getEpicById (Integer integer) {
        Epic epic = epicsList.get(integer);
        return epic;
    }

    public void addTaskObj (Task task) {
        task.setIdNum(idCounter);
        idCounter++;
        tasksList.put(task.getIdNum(), task);
    }

    public void addSubtaskObj (Subtask subtask) {
        subtask.setIdNum(idCounter);
        idCounter++;
        subtasksList.put(subtask.getIdNum(), subtask);
    }

    public void addEpicObj (Epic epic) {
        epic.setIdNum(idCounter);
        idCounter++;
        epicsList.put(epic.getIdNum(), epic);
    }

    public void updTask(Task task) {
        tasksList.put(task.getIdNum(), task);
    }

    public void updSubtask(Subtask subtask) {
        subtasksList.put(subtask.getIdNum(), subtask);
    }

    public void updEpic(Epic epic) {
        epicsList.put(epic.getIdNum(), epic);
    }

    public void deleteTaskById(Integer id) {
        tasksList.remove(id);
    }

    public void deleteSubtaskById(Integer id) {
        subtasksList.remove(id);
    }

    public void deleteEpicById(Integer id) {
        epicsList.remove(id);
    }

    public ArrayList<Subtask> getAllSubtasksOfOneEpic(Epic epic) {
        ArrayList<Integer> subtasksIds = epic.getSubtasksId();
        ArrayList<Subtask> listOfSubtasksOneEpic = new ArrayList<>();
        for (int i = 0; i < subtasksIds.size(); i++) {
            listOfSubtasksOneEpic.add(subtasksList.get(subtasksIds.get(i)));
        }
        return listOfSubtasksOneEpic;
    }

    public void updateEpicStatus (Epic epic) {
        ArrayList<Integer> subtasksIds = epic.getSubtasksId();
        ArrayList<Subtask> listOfSubtasksOneEpic = this.getAllSubtasksOfOneEpic(epic);
        boolean allSubtasksNEW = false;
        boolean allSubtasksDONE = false;

        for(Subtask subtask : listOfSubtasksOneEpic) {
           if(subtask.getStatus() == Status.NEW) {
               allSubtasksNEW = true;
           } else {
               allSubtasksNEW = false;
               break;
           }
        }

        for(Subtask subtask : listOfSubtasksOneEpic) {
            if(subtask.getStatus() == Status.DONE) {
                allSubtasksDONE = true;
            } else {
                allSubtasksDONE = false;
                break;
            }
        }

        if (subtasksIds.size() == 0 || allSubtasksNEW) {
            System.out.println("У этого эпика нет подзадач либо они имеют статут NEW");
            epic.setStatus(Status.NEW);
        } else if (allSubtasksDONE) {
            System.out.println("Все подзадачи эпика завершены.");
            epic.setStatus(Status.DONE);
        } else {
            System.out.println("Эпик к процессе выполнения.");
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
