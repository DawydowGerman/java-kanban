package tasks;

import main.kanban1.java.src.status.Status;
import main.kanban1.java.src.tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    Task task;
    Task task0;

    @BeforeEach
    public void beforeEach() {
        task = new Task("task","to do something");
        task0 = new Task("task","to do something other");
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
        Assertions.assertEquals(task, task0);
    }

    @Test
    void testHashCodeMethodTest() {
        Assertions.assertEquals(task.hashCode(), task0.hashCode());
    }
}