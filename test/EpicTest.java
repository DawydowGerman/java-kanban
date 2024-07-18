package main.kanban1.java.test;

import main.kanban1.java.src.tasks.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class EpicTest {
    Epic epic;

    @BeforeEach
    public void beforeEach() {
        epic = new Epic("subtask","to do something");
    }

    @Test
    void shouldEpicsBeEqualWhenSameId() {
        Epic epic0 = new Epic();
        epic0.setIdNum(1);

        Epic epic1 = new Epic();
        epic1.setIdNum(1);

        Assertions.assertEquals(epic0, epic1);
    }

    @Test
    void epicCannotAddedToItself() {
        Epic epic0 = new Epic();
        epic0.setIdNum(1);
        epic0.linkSubtaskToEpic(1);
        List<Integer> numberOfSubtasks = epic0.getSubtasksId();
        Assertions.assertEquals(numberOfSubtasks.size(),0);
    }

    @Test
    void linkSubtaskToEpicAndGetSubtasksIdMethodsTest() {
        epic.linkSubtaskToEpic(35);
        ArrayList<Integer> list = epic.getSubtasksId();
        Assertions.assertEquals(list.get(0), 35);
    }

    @Test
    void cleanSubtaskIds() {
        epic.linkSubtaskToEpic(35);
        ArrayList<Integer> list = epic.getSubtasksId();
        epic.cleanSubtaskIds();
        Assertions.assertEquals(list.size(), 0);
    }
}