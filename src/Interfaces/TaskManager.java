package main.kanban1.java.src.Interfaces;

import main.kanban1.java.src.exceptions.OvelapException;
import main.kanban1.java.src.tasks.Epic;
import main.kanban1.java.src.tasks.Subtask;
import main.kanban1.java.src.tasks.Task;

import java.util.*;

public interface TaskManager {

    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtasks();

    ArrayList<Epic> getEpics();

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

    ArrayList<Subtask> getAllSubtasksOfOneEpic(Epic epic);

    void updateEpicStatus(Epic epic);

    ArrayList<Task> getPrioritizedTasks();

    boolean checkIntersections(Task task0, Task task1);

    void setAllSubtasksNEWTrue();

    void someSubtasksNotNEW();

    void setAllSubtasksDONETrue();

    void someSubtasksNotDONE();

    void save();



}
