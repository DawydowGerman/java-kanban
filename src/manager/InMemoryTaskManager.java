package main.kanban1.java.src.manager;
import main.kanban1.java.src.Interfaces.HistoryManager;
import main.kanban1.java.src.Interfaces.TaskManager;
import main.kanban1.java.src.exceptions.OvelapException;
import main.kanban1.java.src.tasks.Epic;
import main.kanban1.java.src.status.Status;
import main.kanban1.java.src.tasks.Subtask;
import main.kanban1.java.src.tasks.Task;
import main.kanban1.java.src.util.TasksComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {
    private static int idCounter = 1;
    private HashMap<Integer, Task> tasksList = new HashMap<>();
    private HashMap<Integer, Subtask> subtasksList = new HashMap<>();
    private HashMap<Integer, Epic> epicsList = new HashMap<>();
    private HistoryManager inMemoryHistoryManagerObj;
    private TreeSet<Task> treeSet = new TreeSet<Task>(new TasksComparator());
    private boolean intersectionsValidator = false;
    private boolean allSubtasksNEW = false;
    private boolean allSubtasksDONE = false;
    private boolean someSubtasksNotNEW = false;
    private boolean someSubtasksNotDONE = false;

    public InMemoryTaskManager() {
    }

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
        treeSet.removeAll(getTasks());
        tasksList.clear();
        System.out.println("Все задачи удалены.");
    }

    @Override
    public void deleteAllSubtasks() {
        treeSet.removeAll(getSubtasks());
        epicsList.values()
            .forEach(i -> {
                updateEpicStatus(i);
                i.cleanSubtaskIds();
            });
        subtasksList.clear();
    }

    @Override
    public void deleteAllEpics() {
        treeSet.removeAll(getSubtasks());
        treeSet.removeAll(getEpics());
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
        if (task.getStartTime() != null && task.getDuration() != null) {
            ArrayList<Task> taskList = this.getTasks();
            taskList.forEach(i -> {
                                if (checkIntersections(task, i)) {
                                    throw new OvelapException("Добавляемая задача пересекается с другой");
                                }
                            });
            if (intersectionsValidator) {
                return;
            }
            task.setIdNum(idCounter);
            idCounter++;
            tasksList.put(task.getIdNum(), task);
            treeSet.add(task);
        }
    }

    @Override
    public void addSubtaskObj(Subtask subtask) {
        if (subtask.getStartTime() != null && subtask.getDuration() != null) {
            ArrayList<Subtask> subsList = this.getSubtasks();
            subsList.forEach(i -> {
                        try {
                            if (checkIntersections(subtask, i)) {
                                throw new OvelapException("Добавляемая задача пересекается с другой");
                            }
                        } catch (OvelapException e) {
                            System.out.println(e.getMessage());
                        }
                    });
            if (intersectionsValidator) {
                return;
            }
            subtask.setIdNum(idCounter);
            idCounter++;
            subtasksList.put(subtask.getIdNum(), subtask);
            treeSet.add(subtask);
        }
    }

    @Override
    public void addEpicObj(Epic epic) {
        epic.setIdNum(idCounter);
        idCounter++;
        epicsList.put(epic.getIdNum(), epic);
    }

    @Override
    public void updTask(Task task) {
        if (task.getStartTime() != null && task.getDuration() != null) {
            ArrayList<Task> taskList = this.getTasks();
            taskList.forEach(i -> {
                        try {
                            if (checkIntersections(task, i)) {
                                throw new OvelapException("Добавляемая задача пересекается с другой");
                            }
                        } catch (OvelapException e) {
                            System.out.println(e.getMessage());
                        }
                    });
            if (intersectionsValidator) {
                return;
            }
            tasksList.put(task.getIdNum(), task);
            treeSet.add(task);
        }
    }

    @Override
    public void updSubtask(Subtask subtask) {
        if (subtask.getStartTime() != null && subtask.getDuration() != null) {
            ArrayList<Subtask> subsList = this.getSubtasks();
            subsList.forEach(i -> {
                        try {
                            if (checkIntersections(subtask, i)) {
                                throw new OvelapException("Добавляемая задача пересекается с другой");
                            }
                        } catch (OvelapException e) {
                            System.out.println(e.getMessage());
                        }
                    });
            if (intersectionsValidator) {
                return;
            }
            subtasksList.put(subtask.getIdNum(), subtask);
            treeSet.add(subtask);
        }
    }

    @Override
    public void updEpic(Epic epic) {
        epicsList.put(epic.getIdNum(), epic);
    }

    @Override
    public void deleteTaskById(Integer id) {
        Task task = tasksList.get(id);
        if (task == null) return;
        treeSet.remove(task);
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
        treeSet.remove(subtask);
        subtasksList.remove(id);
        inMemoryHistoryManagerObj.remove(id);
    }

    @Override
    public void deleteEpicById(Integer id) {
        Epic epic = epicsList.get(id);
        if (epic == null) return;
        ArrayList<Integer> listOfSubtasksIds = epic.getSubtasksId();
        listOfSubtasksIds.forEach(i -> subtasksList.remove(i));
        epicsList.remove(id);
        inMemoryHistoryManagerObj.remove(id);
    }

    @Override
    public ArrayList<Subtask> getAllSubtasksOfOneEpic(Epic epic) {
        ArrayList<Integer> subtasksIds = epic.getSubtasksId();
        ArrayList<Subtask> listOfSubtasksOneEpic = new ArrayList<>();
        subtasksIds.forEach(i -> listOfSubtasksOneEpic.add(subtasksList.get(i)));
        return listOfSubtasksOneEpic;
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        ArrayList<Integer> subtasksIds = epic.getSubtasksId();
        ArrayList<Subtask> listOfSubtasksOneEpic = this.getAllSubtasksOfOneEpic(epic);
        listOfSubtasksOneEpic.forEach(i -> {
                if (i.getStatus() == Status.NEW) {
                    setAllSubtasksNEWTrue();
                } else {
                    someSubtasksNotNEW();
                }
            });

        if (someSubtasksNotNEW) {
            allSubtasksNEW = false;
        }
        listOfSubtasksOneEpic.forEach(i -> {
                    if (i.getStatus() == Status.DONE) {
                        setAllSubtasksDONETrue();
                    } else {
                        someSubtasksNotDONE();
                    }
                });
        if (someSubtasksNotDONE) {
            allSubtasksDONE = false;
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

    @Override
    public ArrayList<Task> getPrioritizedTasks() {
        return new ArrayList<>(treeSet);
    }

    @Override
    public boolean checkIntersections(Task task0, Task task1) {
        boolean result = false;
        if ((task0.getStartTime().isBefore(task1.getEndTime()) ||
            task0.getStartTime().equals(task1.getEndTime())) &&
            (task0.getEndTime().isAfter(task1.getStartTime()) ||
            task0.getEndTime().equals(task1.getStartTime()))) {
            result = true;
            intersectionsValidator = true;
            } else {
            intersectionsValidator = false;
        }
        return result;
    }

    @Override
    public void setAllSubtasksNEWTrue() {
        allSubtasksNEW = true;
    }

    @Override
    public void someSubtasksNotNEW() {
        someSubtasksNotNEW = true;
    }

    @Override
    public void setAllSubtasksDONETrue() {
        allSubtasksDONE = true;
    }

    @Override
    public void someSubtasksNotDONE() {
        someSubtasksNotDONE = true;
    }

    @Override
    public void save() {
    }
}
