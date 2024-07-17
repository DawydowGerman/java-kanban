package utilClass;

import main.kanban1.java.src.Interfaces.HistoryManager;
import main.kanban1.java.src.Interfaces.TaskManager;
import main.kanban1.java.src.tasks.Task;
import main.kanban1.java.src.util.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

class ManagersTest {
    Task task;
    Managers managers;
    TaskManager taskManager;
    HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        task = new Task("task","to do something");
        managers = new Managers();
        taskManager = managers.getDefault();
        taskManager.addTaskObj(task);
        historyManager = managers.getDefaultHistory();
    }

    @Test
    void getDefaultMethodReturnsReadyForUseInMemoryTaskManager() {
        List<Task> listOfTasks = taskManager.getTasks();
        Assertions.assertEquals(listOfTasks.size(), 1);
    }

    @Test
    void getDefaultHistoryMethodReturnsReadyForUseInMemoryHistoryManager() {
        taskManager.getTaskById(task.getIdNum());
        List<Task> listOfTasks = historyManager.getHistory();
        Assertions.assertEquals(listOfTasks.size(), 1);
    }
}