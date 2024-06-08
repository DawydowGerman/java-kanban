package main.Kanban;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    static int idCounter = 1;
    HashMap<Integer, Task> tasksList = new HashMap<>();
    HashMap<Integer, Subtask> subtasksList = new HashMap<>();
    HashMap<Integer, Epic> epicsList = new HashMap<>();

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> listOfTasks = new ArrayList<>();
        for (Task task : tasksList.values()) {
            listOfTasks.add(task);
        }
        return listOfTasks;
    }

    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> listOfSubtasks = new ArrayList<>();
        for (Subtask subtask : subtasksList.values()) {
            listOfSubtasks.add(subtask);
        }
        return listOfSubtasks;
    }

    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> listOfEpics = new ArrayList<>();
        for (Epic epic : epicsList.values()) {
            listOfEpics.add(epic);
        }
        return listOfEpics;
    }

    public void deleteAllTasks() {
        tasksList.clear();
        System.out.println("Все задачи удалены.");
    }

    public void deleteAllSubtasks() {
        subtasksList.clear();
        System.out.println("Все подзадачи удалены.");
    }

    public void deleteAllEpics() {
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

    public void printAllTasks() {
        if (tasksList.isEmpty()) {
            System.out.println("Список задач пуст");
            return;
        }
        for (Task task : tasksList.values()) {
            System.out.println("Название задачи " + task.getName());
            System.out.println("Описание задачи " + task.getDescription());
            System.out.println("Номер задачи " + task.getIdNum());
            System.out.println("Статус выполнения задачи " + task.getStatus());
            System.out.println();
        }
    }

    public void printAllSubtasks() {
        if (subtasksList.isEmpty()) {
            System.out.println("Список подзадач пуст");
            return;
        }
        for (Subtask subtask : subtasksList.values()) {
            System.out.println("Название задачи " + subtask.getName());
            System.out.println("Описание задачи " + subtask.getDescription());
            System.out.println("Номер задачи " + subtask.getIdNum());
            System.out.println("Статус выполнения задачи " + subtask.getStatus());
            System.out.println();
        }
    }

    public void printAllEpics() {
        if (epicsList.isEmpty()) {
            System.out.println("Список подзадач пуст");
            return;
        }
        for (Epic epic : epicsList.values()) {
            System.out.println("Название задачи " + epic.getName());
            System.out.println("Описание задачи " + epic.getDescription());
            System.out.println("Номер задачи " + epic.getIdNum());
            System.out.println("Статус выполнения задачи " + epic.getStatus());
            System.out.println();
        }
    }

    public ArrayList<Subtask> getAllSubtasksOfOneEpic(Epic epic) {
        ArrayList<Integer> subtasksIds = epic.getSubtasksId();
        ArrayList<Subtask> listOfSubtasksOneEpic = new ArrayList<>();
        for (int i = 0; i < subtasksIds.size(); i++) {
            listOfSubtasksOneEpic.add(subtasksList.get(subtasksIds.get(i)));
        }
        return listOfSubtasksOneEpic;
    }

    public void manageEpicStatus (Epic epic) {
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
