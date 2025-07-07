package ru.practicum.kanban.manager;

import ru.practicum.kanban.exceptions.ValidationException;
import ru.practicum.kanban.interfaces.HistoryManager;
import ru.practicum.kanban.interfaces.TaskManager;
import ru.practicum.kanban.exceptions.OvelapException;
import ru.practicum.kanban.tasks.Epic;
import ru.practicum.kanban.status.Status;
import ru.practicum.kanban.tasks.Subtask;
import ru.practicum.kanban.tasks.Task;
import ru.practicum.kanban.util.TasksComparator;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class InMemoryTaskManager implements TaskManager {
    private static int idCounter = 1;
    private Map<Integer, Task> tasksList = new HashMap<>();
    private Map<Integer, Subtask> subtasksList = new HashMap<>();
    private Map<Integer, Epic> epicsList = new HashMap<>();
    private HistoryManager historyManager;
    private Set<Task> treeSet = new TreeSet<>(new TasksComparator());

    public InMemoryTaskManager() {
    }

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasksList.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasksList.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epicsList.values());
    }

    @Override
    public void deleteAllTasks() {
        treeSet.removeAll(getTasks());
        tasksList.clear();
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
    public Task getTaskById(Integer id) {
        Task task = tasksList.get(id);
        if (task == null) return null;
        historyManager.add(task);
        return task;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtask = subtasksList.get(id);
        if (subtask == null) return null;
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = epicsList.get(id);
        if (epic == null) return null;
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void addTaskObj(Task task) {
        validateNewTask(task);
        task.setIdNum(idCounter);
        idCounter++;
        tasksList.put(task.getIdNum(), task);
        treeSet.add(task);
    }

    @Override
    public void addSubtaskObj(Subtask subtask) {
        validateNewTask(subtask);
        subtask.setIdNum(idCounter);
        idCounter++;
        subtasksList.put(subtask.getIdNum(), subtask);
        treeSet.add(subtask);
    }

    @Override
    public void addEpicObj(Epic epic) {
        epic.setIdNum(idCounter);
        idCounter++;
        epicsList.put(epic.getIdNum(), epic);
    }

    @Override
    public void updTask(Task task) {
        validateUpdatedTask(task);
        tasksList.put(task.getIdNum(), task);
        treeSet.add(task);
    }

    @Override
    public void updSubtask(Subtask subtask) {
        validateUpdatedTask(subtask);
        subtasksList.put(subtask.getIdNum(), subtask);
        treeSet.add(subtask);
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
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        Subtask subtask = subtasksList.get(id);
        if (subtask == null) return;
        Integer epicId = subtask.getEpicId();
        Epic epic = epicsList.get(epicId);
        epic.deleteSubtaskById(id);
        this.updateEpicStatus(epic);
        treeSet.remove(subtask);
        subtasksList.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(Integer id) {
        Epic epic = epicsList.get(id);
        if (epic == null) return;
        List<Integer> listOfSubtasksIds = epic.getSubtasksId();
        listOfSubtasksIds.forEach(i -> subtasksList.remove(i));
        epicsList.remove(id);
        historyManager.remove(id);
    }

    @Override
    public List<Subtask> getAllSubtasksOfOneEpic(Epic epic) {
        List<Integer> subtasksIds = epic.getSubtasksId();
        List<Subtask> listOfSubtasksOneEpic = new ArrayList<>();
        subtasksIds.forEach(i -> listOfSubtasksOneEpic.add(subtasksList.get(i)));
        return listOfSubtasksOneEpic;
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        List<Subtask> subtasks = this.getAllSubtasksOfOneEpic(epic);
        if (subtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        boolean allNew = subtasks.stream()
                .allMatch(subtask -> subtask.getStatus() == Status.NEW);
        boolean allDone = subtasks.stream()
                .allMatch(subtask -> subtask.getStatus() == Status.DONE);
        if (allNew) {
            System.out.println("У этого эпика нет подзадач либо они имеют статут NEW");
            epic.setStatus(Status.NEW);
        } else if (allDone) {
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

    private void validateNewTask(Task task) {
        checkStartTimeDuration(task);
        List<? extends Task> taskList;
        if (task instanceof Subtask) {
            taskList = this.getSubtasks();
        } else {
            taskList = this.getTasks();
        }
        taskList.forEach(i -> {
            if (checkIntersections(task, i)) {
                throw new OvelapException("Добавляемая задача пересекается с другой");
            }
        });
    }

    private void validateUpdatedTask(Task task) {
        checkStartTimeDuration(task);
        List<? extends Task> taskList;
        if (task instanceof Subtask) {
            taskList = this.getSubtasks();
        } else {
            taskList = this.getTasks();
        }
        taskList.forEach(i -> {
            if (!task.equals(i) && checkIntersections(task, i)) {
                throw new OvelapException("Добавляемая задача пересекается с другой");
            }
        });
    }

    private void checkStartTimeDuration(Task task){
        if (task.getStartTime() == null || task.getDuration() == null) {
            throw new ValidationException("Добавляемая задача должна иметь начальный момент и продолжительность");
        }
    }

    @Override
    public boolean checkIntersections(Task task0, Task task1) {
        boolean result = false;
        if ((task0.getStartTime().isBefore(task1.getEndTime()) ||
              task0.getStartTime().equals(task1.getEndTime())) &&
             (task0.getEndTime().isAfter(task1.getStartTime()) ||
              task0.getEndTime().equals(task1.getStartTime()))) {
            result = true;
        }
        return result;
    }

    @Override
    public void save() {
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}