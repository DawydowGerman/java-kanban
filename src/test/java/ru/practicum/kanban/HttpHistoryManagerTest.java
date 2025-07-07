package ru.practicum.kanban;

import com.google.gson.Gson;
import ru.practicum.kanban.api.HttpTaskServer;
import ru.practicum.kanban.interfaces.HistoryManager;
import ru.practicum.kanban.interfaces.TaskManager;
import ru.practicum.kanban.manager.InMemoryTaskManager;
import ru.practicum.kanban.tasks.Task;
import ru.practicum.kanban.util.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HttpHistoryManagerTest {
    private static final String SERVER_URL = "http://localhost:8080";
    private static final String HISTORY_ENDPOINT = SERVER_URL + "/history";

    private final Managers managers = new Managers();
    private final HistoryManager historyManager = managers.getDefaultHistory();
    private final Gson gson = managers.getGson();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final TaskManager manager = new InMemoryTaskManager(historyManager);
    private final HttpTaskServer taskServer = new HttpTaskServer(manager, gson);

    public HttpHistoryManagerTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
        manager.deleteAllTasks();
        manager.deleteAllSubtasks();
        manager.deleteAllEpics();
        taskServer.start();
    }

    @AfterEach
    void shutDown() {
        taskServer.stop();
    }

    @Test
    void getHistoryWhenHistoryEmptyTest() throws IOException, InterruptedException {
        HttpRequest request = createGetRequest(HISTORY_ENDPOINT);
        HttpResponse<String> response = sendRequest(request);

        Assertions.assertEquals(404, response.statusCode());
        Assertions.assertEquals(response.body(), "Список истории пуст.");
    }

    @Test
    void getHistoryTest() throws IOException, InterruptedException {
        Task task0 = createTestTask("task0", "to do something", 2024, 3, 15, 16, 32, 60);
        Task task1 = createTestTask("task1", "to do something else", 2022, 3, 15, 16, 32, 60);

        manager.addTaskObj(task0);
        manager.addTaskObj(task1);
        manager.getTaskById(task0.getIdNum());
        manager.getTaskById(task1.getIdNum());

        List<Task> expectedHistory = manager.getHistory();
        String expectedJson = gson.toJson(expectedHistory);

        HttpRequest request = createGetRequest(HISTORY_ENDPOINT);
        HttpResponse<String> response = sendRequest(request);

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(expectedJson, response.body());
    }

    @Test
    void wrongMethodTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(""))
                .uri(URI.create(HISTORY_ENDPOINT))
                .build();
        HttpResponse<String> response = sendRequest(request);

        Assertions.assertEquals(500, response.statusCode());
        Assertions.assertEquals("Некорректный метод!", response.body());
    }

    private HttpRequest createGetRequest(String url) {
        return HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException {
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private Task createTestTask(String name, String description,
                                int year, int month, int day,
                                int hour, int minute, int duration) {
        Task task = new Task(name, description);
        task.setStartTime(year, month, day, hour, minute);
        task.setDuration(duration);
        task.setEndTime(task.getStartTime().plus(task.getDuration()));
        return task;
    }
}