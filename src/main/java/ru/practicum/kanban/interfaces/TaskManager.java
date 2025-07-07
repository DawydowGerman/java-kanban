package ru.practicum.kanban.interfaces;

import ru.practicum.kanban.exceptions.OvelapException;
import ru.practicum.kanban.tasks.Epic;
import ru.practicum.kanban.tasks.Subtask;
import ru.practicum.kanban.tasks.Task;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    List<Task> getTasks();

    List<Subtask> getSubtasks();

    List<Epic> getEpics();

    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEpics();

    Task getTaskById(Integer integer);

    Subtask getSubtaskById(Integer integer);

    Epic getEpicById(Integer integer);

    void addTaskObj(Task task) throws OvelapException;

    void addSubtaskObj(Subtask subtask);

    void addEpicObj(Epic epic);

    void updTask(Task task);

    void updSubtask(Subtask subtask);

    void updEpic(Epic epic);

    void deleteTaskById(Integer id);

    void deleteSubtaskById(Integer id);

    void deleteEpicById(Integer id);

    List<Subtask> getAllSubtasksOfOneEpic(Epic epic);

    void updateEpicStatus(Epic epic);

    ArrayList<Task> getPrioritizedTasks();

    boolean checkIntersections(Task task0, Task task1);

    void save();

    List<Task> getHistory();
}