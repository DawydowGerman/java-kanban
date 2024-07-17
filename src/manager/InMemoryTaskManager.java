package main.kanban1.java.src.manager;

import main.kanban1.java.src.Interfaces.HistoryManager;
import main.kanban1.java.src.Interfaces.TaskManager;
import main.kanban1.java.src.tasks.Epic;
import main.kanban1.java.src.status.Status;
import main.kanban1.java.src.tasks.Subtask;
import main.kanban1.java.src.tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
// import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private static int idCounter = 1;
    private HashMap<Integer, Task> tasksList = new HashMap<>();
    private HashMap<Integer, Subtask> subtasksList = new HashMap<>();
    private HashMap<Integer, Epic> epicsList = new HashMap<>();
    private HistoryManager inMemoryHistoryManagerObj;

    public InMemoryTaskManager(HistoryManager iMHMobj) {
        this.inMemoryHistoryManagerObj = iMHMobj;
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasksList.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasksList.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epicsList.values());
    }

    @Override
    public void deleteAllTasks() {
        tasksList.clear();
        System.out.println("Все задачи удалены.");
    }

    @Override
    public void deleteAllSubtasks() {
        for (Epic epic : epicsList.values()) {
            this.updateEpicStatus(epic);
            epic.cleanSubtaskIds();
        }
        subtasksList.clear();
    }

    @Override
    public void deleteAllEpics() {
        subtasksList.clear();
        epicsList.clear();
    }

    @Override
    public Task getTaskById(Integer integer) {
        Task task = tasksList.get(integer);
        if (task == null) return null;
        inMemoryHistoryManagerObj.add(task);
        return task;
    }

    @Override
    public Subtask getSubtaskById(Integer integer) {
        Subtask subtask = subtasksList.get(integer);
        if (subtask == null) return null;
        inMemoryHistoryManagerObj.add(subtask);
        return subtask;
    }

    @Override
    public Epic getEpicById(Integer integer) {
        Epic epic = epicsList.get(integer);
        if (epic == null) return null;
        inMemoryHistoryManagerObj.add(epic);
        return epic;
    }

    @Override
    public void addTaskObj(Task task) {
        task.setIdNum(idCounter);
        idCounter++;
        tasksList.put(task.getIdNum(), task);
    }

    @Override
    public void addSubtaskObj(Subtask subtask) {
        subtask.setIdNum(idCounter);
        idCounter++;
        subtasksList.put(subtask.getIdNum(), subtask);
    }

    @Override
    public void addEpicObj(Epic epic) {
        epic.setIdNum(idCounter);
        idCounter++;
        epicsList.put(epic.getIdNum(), epic);
    }

    @Override
    public void updTask(Task task) {
        tasksList.put(task.getIdNum(), task);
    }

    @Override
    public void updSubtask(Subtask subtask) {
        subtasksList.put(subtask.getIdNum(), subtask);
    }

    @Override
    public void updEpic(Epic epic) {
        epicsList.put(epic.getIdNum(), epic);
    }

    @Override
    public void deleteTaskById(Integer id) {
        tasksList.remove(id);
        inMemoryHistoryManagerObj.remove(id);
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        Subtask subtask = subtasksList.get(id);
        if (subtask == null) return;
        int epicId = subtask.getEpicId();
        Epic epic = epicsList.get(epicId);
        epic.deleteSubtaskId(id);
        this.updateEpicStatus(epic);
        subtasksList.remove(id);
        inMemoryHistoryManagerObj.remove(id);
    }

    @Override
    public void deleteEpicById(Integer id) {
        Epic epic = epicsList.get(id);
        if (epic == null) return;
        ArrayList<Integer> listOfSubtasksIds = epic.getSubtasksId();
        for (Integer a : listOfSubtasksIds) {
            subtasksList.remove(a);
        }
        epicsList.remove(id);
        inMemoryHistoryManagerObj.remove(id);
    }

    @Override
    public ArrayList<Subtask> getAllSubtasksOfOneEpic(Epic epic) {
        ArrayList<Integer> subtasksIds = epic.getSubtasksId();
        ArrayList<Subtask> listOfSubtasksOneEpic = new ArrayList<>();
        for (int i = 0; i < subtasksIds.size(); i++) {
            listOfSubtasksOneEpic.add(subtasksList.get(subtasksIds.get(i)));
        }
        return listOfSubtasksOneEpic;
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        ArrayList<Integer> subtasksIds = epic.getSubtasksId();
        ArrayList<Subtask> listOfSubtasksOneEpic = this.getAllSubtasksOfOneEpic(epic);
        boolean allSubtasksNEW = false;
        boolean allSubtasksDONE = false;

        for (Subtask subtask : listOfSubtasksOneEpic) {
           if (subtask.getStatus() == Status.NEW) {
               allSubtasksNEW = true;
           } else {
               allSubtasksNEW = false;
               break;
           }
        }

        for (Subtask subtask : listOfSubtasksOneEpic) {
            if (subtask.getStatus() == Status.DONE) {
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
