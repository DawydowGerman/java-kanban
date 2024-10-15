package main.kanban1.java.test;

import main.kanban1.java.src.status.Status;
import main.kanban1.java.src.tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

class TaskTest {
    Task task;
    Task task0;
    Task task1;
    Task task2;

    @BeforeEach
    public void beforeEach() {
        task = new Task("task","to do something");
        task0 = new Task("task","to do something other");
        task1 = new Task("task","to do something");
        task2 = new Task("task","to do something");
    }

    @Test
    void shouldTasksBeEqualWhenSameId() {
        Task task0 = new Task();
        task0.setIdNum(1);
        Task task1 = new Task();
        task1.setIdNum(1);
        Assertions.assertEquals(task0, task1);
    }

    @Test
    void getNameMethodTest() {
        Assertions.assertEquals(task.getName(), "task");
    }

    @Test
    void getDescriptionMethodTest() {
        Assertions.assertEquals(task.getDescription(), "to do something");
    }

    @Test
    void getIdNumMethodTest() {
        Assertions.assertEquals(task.getIdNum(), 0);
    }

    @Test
    void getStatusMethodTest() {
        Assertions.assertEquals(task.getStatus(), Status.NEW);
    }

    @Test
    void getDurationMethodTest() {
        task0.setDuration(30);
        Assertions.assertEquals(task0.getDuration().toMinutes(), 30);
    }

    @Test
    void getStartTimeMethodTest() {
        task0.setStartTime(2022,02,12,01,34);
        LocalDateTime testDateTime = LocalDateTime.of(2022,02,12,01,34);
        Assertions.assertEquals(task0.getStartTime(), testDateTime);
    }

    @Test
    void getEndTimeMethodTest() {
        task1.setStartTime(2022,02,12,01,34);
        task1.setDuration(30);
        LocalDateTime task1EndTime = task1.getEndTime();
        LocalDateTime testDateTime = LocalDateTime.of(2022,02,12,02,04);
        Assertions.assertEquals(task1EndTime, testDateTime);
    }

    @Test
    void setNameMethodTest() {
        task.setName("Another task");
        Assertions.assertEquals(task.getName(), "Another task");
    }

    @Test
    void setDescriptionMethodTest() {
        task.setDescription("Another description");
        Assertions.assertEquals(task.getDescription(), "Another description");
    }

    @Test
    void setIdNumMethodTest() {
        task.setIdNum(24);
        Assertions.assertEquals(task.getIdNum(),24);
    }

    @Test
    void setStatusMethodTest() {
        task.setStatus(Status.DONE);
        Assertions.assertEquals(task.getStatus(),Status.DONE);
    }

    @Test
    void testEqualsMethodTest() {
        Assertions.assertEquals(task1, task2);
    }

    @Test
    void testHashCodeMethodTest() {
        Assertions.assertEquals(task1.hashCode(), task2.hashCode());
    }
}