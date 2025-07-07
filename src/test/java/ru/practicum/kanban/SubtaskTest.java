package ru.practicum.kanban;

import ru.practicum.kanban.tasks.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNull;

class SubtaskTest {
    Subtask subtask;
    Subtask subtask0;
    Subtask subtask1;

    @BeforeEach
    public void beforeEach() {
        subtask0 = new Subtask();
        subtask0.setIdNum(1);
        subtask1 = new Subtask();
        subtask1.setIdNum(1);
        subtask = new Subtask("subtask","to do something");
        subtask.setIdNum(1);
    }

    @Test
    void shouldSubtasksBeEqualWhenSameId() {
        Assertions.assertEquals(subtask0, subtask1);
    }

    @Test
    void subtaskCannotBeEpicForItself() {
        subtask0.setEpicId(1);
        assertNull(subtask0.getEpicId());
    }

    @Test
    void getEpicIdAndSetEpicIdMethodsTest() {
        subtask.setEpicId(35);
        Assertions.assertEquals(subtask.getEpicId(), 35);
    }
}