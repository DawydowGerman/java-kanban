package main.kanban1.java.test;

import main.kanban1.java.src.tasks.Subtask;
import main.kanban1.java.src.tasks.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class EpicTest {

    Epic epic;

    @BeforeEach
    public void beforeEach() {
        epic = new Epic("epic","to do something");
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
        Subtask subtask0 = new Subtask();
        subtask0.setIdNum(1);
        epic0.linkSubtaskToEpic(subtask0);
        List<Integer> numberOfSubtasks = epic0.getSubtasksId();
        Assertions.assertEquals(numberOfSubtasks.size(),0);
    }

    @Test
    void linkSubtaskToEpicAndGetSubtasksIdMethodsTest() {
        Subtask subtask1 = new Subtask(60,2023,02,15,07,11);
        subtask1.setIdNum(334);
        subtask1.getEndTime();
        epic.linkSubtaskToEpic(subtask1);
        ArrayList<Integer> list = epic.getSubtasksId();
        Assertions.assertEquals(list.get(0), 334);
    }

    @Test
    void cleanSubtaskIds() {
        Subtask subtask2 = new Subtask();
        epic.linkSubtaskToEpic(subtask2);
        ArrayList<Integer> list = epic.getSubtasksId();
        epic.cleanSubtaskIds();
        Assertions.assertEquals(list.size(), 0);
    }

    @Test
    void setDurationStartTimeEndTimeTestForDuration() {
        Subtask subtask1 = new Subtask(60,2022,02,15,07,11);
        subtask1.setIdNum(27);
        subtask1.getEndTime();
        Subtask subtask2 = new Subtask(60,2023,02,15,07,11);
        subtask2.setIdNum(99);
        subtask2.getEndTime();
        epic.linkSubtaskToEpic(subtask1);
        epic.linkSubtaskToEpic(subtask2);
        epic.setDurationStartTimeEndTime();
        epic.getDuration();
        Assertions.assertEquals(epic.getDuration().toMinutes(), 120);
    }

    @Test
    void setDurationStartTimeEndTimeTestForStartTime() {
        Subtask subtask1 = new Subtask(60,2022,02,15,07,11);
        subtask1.setIdNum(23);
        Subtask subtask2 = new Subtask(60,2023,02,15,07,11);
        subtask2.setIdNum(56);
        epic.linkSubtaskToEpic(subtask1);
        epic.linkSubtaskToEpic(subtask2);
        epic.setDurationStartTimeEndTime();
        Assertions.assertEquals(epic.getStartTime(), subtask1.getStartTime());
    }

    @Test
    void setDurationStartTimeEndTimeTestForEndTime() {
        Epic epic0 = new Epic();
        epic0.setIdNum(13);
        Subtask subtask3 = new Subtask(60,2022,02,15,07,11);
        subtask3.setIdNum(23);
        Subtask subtask4 = new Subtask(60,2023,02,15,07,11);
        subtask4.setIdNum(56);
        subtask3.getEndTime();
        subtask4.getEndTime();
        epic0.linkSubtaskToEpic(subtask3);
        epic0.linkSubtaskToEpic(subtask4);
        epic0.setDurationStartTimeEndTime();
        LocalDateTime epicsEndTime = epic0.getEndTime();
        LocalDateTime testTime = subtask4.getEndTime();
        Assertions.assertEquals(epicsEndTime, testTime);
    }
}
