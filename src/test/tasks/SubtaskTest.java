package tasks;

import main.kanban1.java.src.tasks.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubtaskTest {
    Subtask subtask;

    @BeforeEach
    public void beforeEach() {
        subtask = new Subtask("subtask","to do something");
    }

    @Test
    void shouldSubtasksBeEqualWhenSameId() {
        Subtask subtask0 = new Subtask();
        subtask0.setIdNum(1);

        Subtask subtask1 = new Subtask();
        subtask1.setIdNum(1);

        Assertions.assertEquals(subtask0, subtask1);
    }

    @Test
    void subtaskCannotBeEpicForItself() {
        Subtask subtask0 = new Subtask();
        subtask0.setIdNum(1);
        subtask0.setEpicId(1);
        Assertions.assertEquals(subtask0.getEpicId(),0);
    }

    @Test
    void getEpicIdAndSetEpicIdMethodsTest() {
        subtask.setEpicId(35);
        Assertions.assertEquals(subtask.getEpicId(), 35);
    }
}