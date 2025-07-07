package ru.practicum.kanban;

import com.google.gson.Gson;
import ru.practicum.kanban.api.HttpTaskServer;
import ru.practicum.kanban.interfaces.HistoryManager;
import ru.practicum.kanban.interfaces.TaskManager;
import ru.practicum.kanban.manager.InMemoryTaskManager;
import ru.practicum.kanban.tasks.Task;
import ru.practicum.kanban.util.Managers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HttpTaskManagerTasksTest {
    private static final String SERVER_URL = "http://localhost:8080";
    private static final String ENDPOINT = SERVER_URL + "/tasks";

    private final Managers managers = new Managers();
    private final HistoryManager historyManager = managers.getDefaultHistory();
    private final Gson gson = managers.getGson();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final TaskManager manager = new InMemoryTaskManager(historyManager);
    private final HttpTaskServer taskServer = new HttpTaskServer(manager, gson);

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteAllTasks();
        manager.deleteAllSubtasks();
        manager.deleteAllEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void getTasksTest() throws IOException, InterruptedException {
        Task task0 = createTestTask("task0", "to do something", 2024, 3, 15, 16, 32, 60);
        Task task1 = createTestTask("task1", "to do something else", 2022, 3, 15, 16, 32, 60);
        manager.addTaskObj(task0);
        manager.addTaskObj(task1);
        List<Task> tasks = manager.getTasks();
        String jsonFormattedTasks = gson.toJson(tasks);
        HttpRequest request = createGetRequest(ENDPOINT);
        HttpResponse<String> response = sendRequest(request);
        String responseBody = response.body();
        int responseStatus = response.statusCode();

        Assertions.assertEquals(responseStatus, 200);
        Assertions.assertEquals(jsonFormattedTasks, responseBody);
    }

    @Test
    public void getTasksWhenListIsEmptyTest() throws IOException, InterruptedException {
        String answer = "Список задач пуст.";
        HttpRequest request = createGetRequest(ENDPOINT);
        HttpResponse<String> response = sendRequest(request);
        String responseBody = response.body();
        int responseStatus = response.statusCode();

        Assertions.assertEquals(responseStatus, 404);
        Assertions.assertEquals(answer, responseBody);
    }

    @Test
    public void getTaskByIdTest() throws IOException, InterruptedException {
        Task task0 = createTestTask("task0", "to do something", 2024, 3, 15, 16, 32, 60);
        manager.addTaskObj(task0);
        Task task = manager.getTaskById(task0.getIdNum());
        String jsonFormattedTask = gson.toJson(List.of(task));
        HttpRequest request = createGetRequest(ENDPOINT + task0.getIdNum());
        HttpResponse<String> response = sendRequest(request);

        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(jsonFormattedTask, response.body());
    }

    @Test
    public void getTaskByIdForWrongNumTest() throws IOException, InterruptedException {
        String answer = "Такой задачи нет.";
        HttpRequest request = createGetRequest(ENDPOINT + "/35");
        HttpResponse<String> response = sendRequest(request);

        Assertions.assertEquals(response.statusCode(), 404);
        Assertions.assertEquals(answer, response.body());
    }

    @Test
    public void addTaskObjTest() throws Exception {
        Task task0 = createTestTask("subtask0", "to do something", 2024, 3, 15, 16, 32, 60);
        task0.setIdNum(3);
        String jsonFormattedTask = gson.toJson(task0);
        HttpResponse<String> response = sendPostRequest(ENDPOINT, jsonFormattedTask);
        List<Task> list = manager.getTasks();
        Task task = manager.getTaskById(list.get(0).getIdNum());
        String taksFromManager = gson.toJson(task);

        Assertions.assertEquals(response.statusCode(), 201);
        Assertions.assertEquals(jsonFormattedTask, taksFromManager);
    }

    @Test
    public void updTaskTest() throws Exception {
        String jsonFormattedTask = "{\n" +
                "  \"name\": \"task0\",\n" +
                "  \"description\": \"to do something\",\n" +
                "  \"idNum\": 3,\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"duration\": \"PT1H\",\n" +
                "  \"startTime\": \"2024-03-15T16:32:00\",\n" +
                "  \"endTime\": \"2024-03-15T17:32:00\"\n" +
                "}";
        HttpResponse<String> response = sendPostRequest(ENDPOINT, jsonFormattedTask);
        Task task = manager.getTaskById(3);
        String taksFromManager = gson.toJson(task);

        Assertions.assertEquals(response.statusCode(), 201);
        Assertions.assertEquals(jsonFormattedTask, taksFromManager);
    }

    @Test
    public void overlapTest() throws Exception {
        String jsonFormattedTask0 = "{\n" +
                "  \"name\": \"task0\",\n" +
                "  \"description\": \"to do something\",\n" +
                "  \"idNum\": 3,\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"duration\": \"PT1H\",\n" +
                "  \"startTime\": \"2024-03-15T16:32:00\",\n" +
                "  \"endTime\": \"2024-03-15T17:32:00\"\n" +
                "}";
        String jsonFormattedTask1 = "{\n" +
                "  \"name\": \"task0\",\n" +
                "  \"description\": \"to do something\",\n" +
                "  \"idNum\": 4,\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"duration\": \"PT1H\",\n" +
                "  \"startTime\": \"2024-03-15T16:32:00\",\n" +
                "  \"endTime\": \"2024-03-15T17:32:00\"\n" +
                "}";
        HttpResponse<String> response = sendPostRequest(ENDPOINT, jsonFormattedTask0);
        HttpResponse<String> response1 = sendPostRequest(ENDPOINT, jsonFormattedTask1);

        Assertions.assertEquals(response1.statusCode(), 406);
        Assertions.assertEquals(response1.body(), "Добавляемая задача переcекается с другой.");
    }

    @Test
    public void deleteTaskWithoutNumTest() throws Exception {
        HttpResponse<String> response = sendDeleteRequest(ENDPOINT);

        Assertions.assertEquals(response.statusCode(), 500);
        Assertions.assertEquals(response.body(), "Не указан номер удаляемой задачи.");
    }

    @Test
    public void deleteNonexistentTaskTest() throws Exception {
        HttpResponse<String> response = sendDeleteRequest(ENDPOINT + "/37");

        Assertions.assertEquals(response.statusCode(), 404);
        Assertions.assertEquals(response.body(), "Удаляемой задачи не существует.");
    }

    @Test
    public void deleteTaskTest() throws Exception {
        Task task = createTestTask("subtask0", "to do something", 2024, 3, 15, 16, 32, 60);
        manager.addTaskObj(task);
        HttpResponse<String> response = sendDeleteRequest(ENDPOINT + "/" + task.getIdNum());

        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(response.body(), "Задача удалена.");
    }

    @Test
    public void wrongMethodTest() throws IOException, InterruptedException {
        URI url = URI.create(ENDPOINT);
        String data = "";
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(data))
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        Assertions.assertEquals(response.statusCode(), 500);
        Assertions.assertEquals(response.body(), "Некорректный метод!");
    }

    private HttpRequest createGetRequest(String url) {
        return HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();
    }

    private HttpResponse<String> sendPostRequest(String url, String body) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create(url))
                .build();
        return sendRequest(request);
    }

    private HttpResponse<String> sendDeleteRequest(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url))
                .build();
        return sendRequest(request);
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