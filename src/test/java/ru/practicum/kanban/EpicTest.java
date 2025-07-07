package ru.practicum.kanban;

import ru.practicum.kanban.tasks.Subtask;
import ru.practicum.kanban.tasks.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

class EpicTest {
    private Epic epic;
    private Epic epicWithSameId;
    private Epic emptyEpic;
    private Subtask subtask1;
    private Subtask subtask2;
    private Subtask subtaskWithEpicId;
    private Subtask nullSubtask;

    @BeforeEach
    public void beforeEach() {
        epic = new Epic("epic", "to do something");
        epic.setIdNum(226);

        epicWithSameId = new Epic("epic", "to do something");
        epicWithSameId.setIdNum(226);
        emptyEpic = new Epic();
        emptyEpic.setIdNum(0);

        subtask1 = new Subtask(60, 2022, 2, 15, 7, 11);
        subtask1.setIdNum(334);
        subtask1.getEndTime();

        subtask2 = new Subtask(60, 2023, 2, 15, 7, 11);
        subtask2.setIdNum(234);
        subtask2.getEndTime();

        subtaskWithEpicId = new Subtask();
        subtaskWithEpicId.setIdNum(226);

        nullSubtask = null;
    }

    @Test
    void shouldEpicsBeEqualWhenSameId() {
        Assertions.assertEquals(epic, epicWithSameId, "Epics with same ID should be equal");
    }

    @Test
    void epicCannotAddedToItself() {
        epic.linkSubtaskToEpic(subtaskWithEpicId);
        List<Integer> numberOfSubtasks = epic.getSubtasksId();
        Assertions.assertEquals(numberOfSubtasks.size(),0);
    }

    @Test
    void linkSubtaskToEpicAndGetSubtasksIdMethodsTest() {
        epic.linkSubtaskToEpic(subtask1);
        List<Integer> list = epic.getSubtasksId();
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals(334, list.get(0));
    }

    @Test
    void cleanSubtaskIds() {
        epic.linkSubtaskToEpic(subtask1);
        epic.cleanSubtaskIds();
        List<Integer> list = epic.getSubtasksId();
        Assertions.assertEquals(list.size(), 0);
    }

    @Test
    void setDurationStartTimeEndTimeTestForDuration() {
        epic.linkSubtaskToEpic(subtask1);
        epic.linkSubtaskToEpic(subtask2);
        epic.setDurationStartTimeEndTime();
        Assertions.assertEquals(120, epic.getDuration().toMinutes());
    }

    @Test
    void setDurationStartTimeEndTimeTestForStartTime() {
        epic.linkSubtaskToEpic(subtask1);
        epic.linkSubtaskToEpic(subtask2);
        epic.setDurationStartTimeEndTime();
        Assertions.assertEquals(subtask1.getStartTime(), epic.getStartTime());
    }

    @Test
    void setDurationStartTimeEndTimeTestForEndTime() {
        epic.linkSubtaskToEpic(subtask1);
        epic.linkSubtaskToEpic(subtask2);
        epic.setDurationStartTimeEndTime();
        Assertions.assertEquals(subtask2.getEndTime(), epic.getEndTime());
    }

    @Test
    void shouldHandleNullSubtask() {
        epic.linkSubtaskToEpic(nullSubtask);
        Assertions.assertTrue(epic.getSubtasksId().isEmpty());
    }

    @Test
    void shouldNotDuplicateSubtasks() {
        epic.linkSubtaskToEpic(subtask1);
        epic.linkSubtaskToEpic(subtask1);
        Assertions.assertEquals(1, epic.getSubtasksId().size());
    }

    @Test
    void shouldHandleEmptySubtasksForTimeCalculation() {
        epic.setDurationStartTimeEndTime();
        Assertions.assertNull(epic.getStartTime());
        Assertions.assertNull(epic.getEndTime());
        Assertions.assertEquals(0, epic.getDuration().toMinutes());
    }

    @Test
    void shouldClearAllSubtasksWhenCleaning() {
        epic.linkSubtaskToEpic(subtask1);
        epic.linkSubtaskToEpic(subtask2);
        int initialSize = epic.getSubtasksId().size();

        epic.cleanSubtaskIds();

        Assertions.assertEquals(0, epic.getSubtasksId().size());
        Assertions.assertTrue(initialSize > 0);
    }
}