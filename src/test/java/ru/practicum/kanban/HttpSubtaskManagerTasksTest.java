package ru.practicum.kanban;

import com.google.gson.Gson;
import ru.practicum.kanban.api.HttpTaskServer;
import ru.practicum.kanban.interfaces.HistoryManager;
import ru.practicum.kanban.interfaces.TaskManager;
import ru.practicum.kanban.manager.InMemoryTaskManager;
import ru.practicum.kanban.tasks.Epic;
import ru.practicum.kanban.tasks.Subtask;
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

public class HttpSubtaskManagerTasksTest {
    private static final String SERVER_URL = "http://localhost:8080";
    private static final String ENDPOINT = SERVER_URL + "/subtasks";

    private final Managers managers = new Managers();
    private final HistoryManager historyManager = managers.getDefaultHistory();
    private final Gson gson = managers.getGson();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final TaskManager manager = new InMemoryTaskManager(historyManager);
    private final HttpTaskServer taskServer = new HttpTaskServer(manager, gson);

    public HttpSubtaskManagerTasksTest() throws IOException {
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
    public void getSubtasksTest() throws IOException, InterruptedException {
        Subtask subtask0 = createTestSubtask("subtask0", "to do something", 2024, 3, 15, 16, 32, 60);
        Subtask subtask1 = createTestSubtask("subtask1", "to do something else", 2022, 3, 15, 16, 32, 60);
        manager.addSubtaskObj(subtask0);
        manager.addSubtaskObj(subtask1);
        List<Subtask> subtasks = manager.getSubtasks();
        String jsonFormattedTasks = gson.toJson(subtasks);
        HttpRequest request = createGetRequest(ENDPOINT);
        HttpResponse<String> response = sendRequest(request);
        String responseBody = response.body();
        int responseStatus = response.statusCode();

        Assertions.assertEquals(responseStatus, 200);
        Assertions.assertEquals(jsonFormattedTasks, responseBody);
    }

    @Test
    public void getSubtasksWhenListIsEmptyTest() throws IOException, InterruptedException {
        String answer = "Список подзадач пуст.";
        HttpRequest request = createGetRequest(ENDPOINT);
        HttpResponse<String> response = sendRequest(request);
        String responseBody = response.body();
        int responseStatus = response.statusCode();

        Assertions.assertEquals(responseStatus, 404);
        Assertions.assertEquals(answer, responseBody);
    }

    @Test
    public void getSubtaskByIdTest() throws IOException, InterruptedException {
        Subtask subtask0 = createTestSubtask("subtask0", "to do something", 2024, 3, 15, 16, 32, 60);
        manager.addSubtaskObj(subtask0);
        Subtask subtask = manager.getSubtaskById(subtask0.getIdNum());
        String jsonFormattedTask = gson.toJson(List.of(subtask));
        HttpRequest request = createGetRequest(ENDPOINT + subtask0.getIdNum());
        HttpResponse<String> response = sendRequest(request);

        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(jsonFormattedTask, response.body());
    }

    @Test
    public void getSubtaskByIdForWrongNumTest() throws IOException, InterruptedException {
        String answer = "Такой подзадачи нет.";
        HttpRequest request = createGetRequest(ENDPOINT + "/35");
        HttpResponse<String> response = sendRequest(request);

        Assertions.assertEquals(response.statusCode(), 404);
        Assertions.assertEquals(answer, response.body());
    }

    @Test
    public void addSubtaskObjTest() throws Exception {
        Subtask subtask0 = createTestSubtask("subtask0", "to do something", 2024, 3, 15, 16, 32, 60);
        subtask0.setIdNum(2);
        String jsonFormattedTask = gson.toJson(subtask0);
        HttpResponse<String> response = sendPostRequest(ENDPOINT, jsonFormattedTask);
        List<Subtask> list = manager.getSubtasks();
        Subtask subtask = manager.getSubtaskById(list.get(0).getIdNum());
        String taksFromManager = gson.toJson(subtask);

        Assertions.assertEquals(response.statusCode(), 201);
        Assertions.assertEquals(jsonFormattedTask, taksFromManager);
    }

    @Test
    public void updSubtaskTest() throws Exception {
        String jsonFormattedTask = "{\n" +
                "  \"epicId\": 0,\n" +
                "  \"name\": \"subtask0\",\n" +
                "  \"description\": \"to do something\",\n" +
                "  \"idNum\": 3,\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"duration\": \"PT1H\",\n" +
                "  \"startTime\": \"2024-03-15T16:32:00\",\n" +
                "  \"endTime\": \"2024-03-15T17:32:00\"\n" +
                "}";
        HttpResponse<String> response = sendPostRequest(ENDPOINT, jsonFormattedTask);
        Subtask subtask = manager.getSubtaskById(3);
        String taksFromManager = gson.toJson(subtask);

        Assertions.assertEquals(response.statusCode(), 201);
        Assertions.assertEquals(jsonFormattedTask, taksFromManager);
    }

    @Test
    public void overlapTest() throws Exception {
        String jsonFormattedTask0 = "{\n" +
                "  \"epicId\": 0,\n" +
                "  \"name\": \"task0\",\n" +
                "  \"description\": \"to do something\",\n" +
                "  \"idNum\": 3,\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"duration\": \"PT1H\",\n" +
                "  \"startTime\": \"2024-03-15T16:32:00\",\n" +
                "  \"endTime\": \"2024-03-15T17:32:00\"\n" +
                "}";
        String jsonFormattedTask1 = "{\n" +
                "  \"epicId\": 0,\n" +
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
        Assertions.assertEquals(response1.body(), "Добавляемая подзадача переcекается с другой.");
    }

    @Test
    public void deleteSubtaskWithoutNumTest() throws Exception {
        HttpResponse<String> response = sendDeleteRequest(ENDPOINT);

        Assertions.assertEquals(response.statusCode(), 500);
        Assertions.assertEquals(response.body(), "Не указан номер удаляемой подзадачи.");
    }

    @Test
    public void deleteNonexistentSubaskTest() throws Exception {
        HttpResponse<String> response = sendDeleteRequest(ENDPOINT + "/37");

        Assertions.assertEquals(response.statusCode(), 404);
        Assertions.assertEquals(response.body(), "Удаляемой подзадачи не существует.");
    }

    @Test
    public void deleteSubtaskTest() throws Exception {
        Subtask subtask0 = createTestSubtask("subtask0", "to do something", 2024, 3, 15, 16, 32, 60);
        Epic epic = new Epic("epic", "desc");
        manager.addSubtaskObj(subtask0);
        manager.addEpicObj(epic);
        subtask0.setEpicId(epic.getIdNum());
        epic.linkSubtaskToEpic(subtask0);
        HttpResponse<String> response = sendDeleteRequest(ENDPOINT + "/" + subtask0.getIdNum());

        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(response.body(), "Подзадача удалена.");
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

    private Subtask createTestSubtask(String name, String description,
                                int year, int month, int day,
                                int hour, int minute, int duration) {
        Subtask subtask = new Subtask(name, description);
        subtask.setStartTime(year, month, day, hour, minute);
        subtask.setDuration(duration);
        subtask.setEndTime(subtask.getStartTime().plus(subtask.getDuration()));
        return subtask;
    }
}