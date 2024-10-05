package main.kanban1.java.src;

import main.kanban1.java.src.Interfaces.HistoryManager;
import main.kanban1.java.src.manager.InMemoryHistoryManager;
import main.kanban1.java.src.tasks.Epic;
import main.kanban1.java.src.tasks.Subtask;
import main.kanban1.java.src.tasks.Task;
import main.kanban1.java.src.Interfaces.TaskManager;
import main.kanban1.java.src.manager.FileBackedTaskManager;
import main.kanban1.java.src.util.Managers;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File file;
        Task task0;
        Task task1;
        FileBackedTaskManager fileBackedTaskManager;
        Managers managers;
        TaskManager taskManager;

        try {
            file = File.createTempFile("tmp", ".txt");
            fileBackedTaskManager = new FileBackedTaskManager(file);

            task0 = new Task("Task0", "Description of the task0");
            task0.setStartTime(2024, 3, 15, 16, 32);
            task0.setDuration(10080);
            task0.setIdNum(34);

            task1 = new Task("Task1", "Description of the task1");
            task1.setStartTime(2024, 3, 22, 16, 32);
            task1.setDuration(60);
            task1.setIdNum(28);

            fileBackedTaskManager.addTaskObj(task0);
            fileBackedTaskManager.addTaskObj(task1);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}



/*
       Managers managers = new Managers();
        TaskManager taskManager = managers.getDefault();

        Subtask subtask0 = new Subtask();
        subtask0.setIdNum(33);
        subtask0.setStartTime(2024, 3,29,12,30);
        subtask0.setDuration(60);

        Subtask subtask1 = new Subtask();
        subtask1.setIdNum(44);
        subtask1.setStartTime(2024, 9,15,12,30);
        subtask1.setDuration(60);

        Epic epic0 = new Epic();
        epic0.setIdNum(55);

        taskManager.addSubtaskObj(subtask0);
        taskManager.addSubtaskObj(subtask1);
        taskManager.addEpicObj(epic0);

        epic0.linkSubtaskToEpic(subtask0);
        epic0.linkSubtaskToEpic(subtask1);

 //       taskManager.deleteEpicById(epic0.getIdNum());

        ArrayList<Subtask> subsList = taskManager.getAllSubtasksOfOneEpic(epic0);
        System.out.println("subsList size is:" + subsList.size());




        /*
 ArrayList<Subtask> subsListManager = taskManager.getSubtasks();
        for (Subtask subtask : subsListManager) {
            System.out.println("subs id in manager are: " + subtask.getIdNum());
        }

        ArrayList<Integer> subsIdListInEpic = epic0.getSubtasksId();
        System.out.println("subs id in Epic are: " + subsIdListInEpic);


        ArrayList<Epic> epicsList = taskManager.getEpics();
        System.out.println("epicsList size is:" + epicsList.size());


         */












/*
        File file;
        Task task;
        Task task2;
        Subtask subtask;
        FileBackedTaskManager fileBackedTaskManager;
        Managers managers;
        TaskManager taskManager;

        managers = new Managers();
        taskManager = managers.getDefault();

        try {
            file = File.createTempFile("tmp", ".txt");
            fileBackedTaskManager = new FileBackedTaskManager(file);

            task = new Task("Task1", "Description");
            task.setStartTime(2024, 10,15,16,32);
            task.setDuration(60);
            task.getEndTime();

            task2 = new Task("Task1", "Description");
            task2.setStartTime(2024, 3,1,16,32);
            task2.setDuration(120);
            task2.getEndTime();

            fileBackedTaskManager.addTaskObj(task);
            fileBackedTaskManager.addTaskObj(task2);

            fileBackedTaskManager.save();

            System.out.println("File path: " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }










        Subtask subtask0 = new Subtask();
        subtask0.setIdNum(33);
        subtask0.setStartTime(2024, 3,29,12,30);
        subtask0.setDuration(60);

        Subtask subtask1 = new Subtask();
        subtask1.setIdNum(44);
        subtask1.setStartTime(2024, 9,15,12,30);
        subtask1.setDuration(60);

        Epic epic0 = new Epic();
        epic0.setIdNum(55);

        epic0.linkSubtaskToEpic(subtask0);
        epic0.linkSubtaskToEpic(subtask1);

        epic0.setDuration();

        System.out.println(epic0.getDuration().toMinutes());










        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefault();

        Task task0 = new Task("Task0", "Description of the task0");
        task0.setStartTime(2024, 3, 15, 16, 32);
        task0.setDuration(10080);
        task0.getEndTime();
        //    System.out.println("task0 endTime is: " + task0.getEndTime());
        // task0 endTime is: 2024-03-22T11:52

        Subtask task1 = new Subtask("Task1", "Description of the task1");
        task1.setStartTime(2024, 3, 22, 16, 33);
        task1.setDuration(100);
        task1.getEndTime();
        //    System.out.println("task1 endTime is: " + task1.getEndTime());
        //     // task1 endTime is: 2024-05-21T18:13

        taskManager.addTaskObj(task0);
        taskManager.addTaskObj(task1);

        task1.setStartTime(2024, 3, 25, 16, 33);
        task1.setDuration(100);
        task1.getEndTime();
        taskManager.addTaskObj(task1);

        ArrayList<Task> taskList = taskManager.getPrioritizedTasks();

        for (Task task : taskList) {
            System.out.println("startTime is: " + task.getStartTime());
        }






        Task task0 = new Task("Task0", "Description of the task0");
        task0.setStartTime(2024, 3,15,16,32);
        task0.setDuration(10080);
        task0.getEndTime();
        System.out.println("task0 endTime is: " + task0.getEndTime());
        // task0 endTime is: 2024-03-22T11:52

        Subtask task1 = new Subtask("Task1", "Description of the task1");
        task1.setStartTime(2024, 3,22,16,33);
        task1.setDuration(100);
        task1.getEndTime();
        System.out.println("task1 endTime is: " + task1.getEndTime());
        // task1 endTime is: 2024-05-21T18:13

        taskManager.addTaskObj(task0);
        taskManager.addTaskObj(task1);

        System.out.println("These two tasks are overlap? - > " +
                taskManager.checkIntersections(task0, task1));





















            // 3,EPIC,null,NEW,null,120,29.03.2024, 12:30,15.09.2024, 13:30,
            // 1,SUBTASK,null,NEW,null,0,60,29.03.2024, 12:30,29.03.2024, 13:30,

            String epic0 = "3,EPIC,null,NEW,Description,120,29.03.2024, 12:30,15.09.2024, 13:30,";
            String sub0 = "1,SUBTASK,null,NEW,Description,3,60,29.03.2024, 12:30,29.03.2024, 13:30,";

            subtask = (Subtask) fileBackedTaskManager.fromString(sub0);
            System.out.println("task name: " + subtask.getName() + "\n" +
                    " task description: " + subtask.getDescription() + "\n" +
                    " task IdNum: " + subtask.getIdNum() + "\n" +
                    " task IdNum: " + subtask.getEpicId() + "\n" +
                    " task Status: " + subtask.getStatus() + "\n" +
                    " task duration: " + subtask.getDuration() + "\n" +
                    " task startTime: " + subtask.getStartTime() + "\n" +
                    " task endTime: " + subtask.getEndTime() + "\n" +
                    " object type " + subtask.getClass().getName()
            );











            // 3,EPIC,null,NEW,null,120,29.03.2024, 12:30,15.09.2024, 13:30,
            // 1,SUBTASK,null,NEW,null,0,60,29.03.2024, 12:30,29.03.2024, 13:30,

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
            String value = "3,EPIC,null,NEW,null,120,29.03.2024, 12:30,15.09.2024, 13:30,";

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

            if(taskType.equals("SUBTASK")) {
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

            System.out.println(
                    "task Id: " + taskId + "\n" +
                    " task type: " + taskType + "\n" +
                    " task Name: " + taskName + "\n" +
                    " task Status: " + status + "\n" +
            //        " task epicId: " + epicId + "\n" +
                    " task duration: " + taskDuration + "\n" +
                    " task startTime: " + endTimeStr + "\n" +
                    " task endTime: " + endTimeStr
            );



            Managers managers = new Managers();
        TaskManager taskManager = managers.getDefault();

        Task task0 = new Task("Task0", "Description of the task0");
        task0.setStartTime(2024, 3, 15, 16, 32);
        task0.setDuration(10080);
        task0.getEndTime();

        Task task1 = new Task("Task1", "Description of the task1");
        task1.setStartTime(2024, 3, 22, 16, 32);
        task1.setDuration(100);
        task1.getEndTime();

        taskManager.addTaskObj(task0);
        taskManager.addTaskObj(task1);

        ArrayList<Task> taskList = taskManager.getTasks();
        System.out.println("taskList size is: " + taskList.size());
 */