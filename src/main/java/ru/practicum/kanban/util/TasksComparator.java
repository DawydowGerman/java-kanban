package ru.practicum.kanban.util;

import ru.practicum.kanban.tasks.Task;
import java.util.Comparator;

public class TasksComparator implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {
        if (task1.getStartTime().isAfter(task2.getStartTime())) {
            return 1;
        } else if (task1.getStartTime().isBefore(task2.getStartTime())) {
            return -1;
        } else {
            return 0;
        }
    }
}