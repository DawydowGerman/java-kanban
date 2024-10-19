package main.kanban1.java.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.kanban1.java.src.API.DurationConverter;
import main.kanban1.java.src.API.HttpTaskServer;
import main.kanban1.java.src.API.LocalDateTimeConverter;
import main.kanban1.java.src.Interfaces.HistoryManager;
import main.kanban1.java.src.Interfaces.TaskManager;
import main.kanban1.java.src.manager.InMemoryTaskManager;
import main.kanban1.java.src.tasks.Epic;
import main.kanban1.java.src.tasks.Subtask;
import main.kanban1.java.src.util.Managers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HttpSubtaskManagerTasksTest {
    Managers managers = new Managers();
    HistoryManager historyManager = managers.getDefaultHistory();
    TaskManager manager = new InMemoryTaskManager(historyManager);
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Duration.class, new DurationConverter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter())
            .create();

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
        Subtask subtask0 = new Subtask("subtask0","to do something");
        subtask0.setStartTime(2024, 3, 15, 16, 32);
        subtask0.setDuration(60);
        subtask0.getEndTime();

        Subtask subtask1 = new Subtask("task0","to do something");
        subtask1.setStartTime(2022, 3, 15, 16, 32);
        subtask1.setDuration(60);
        subtask1.getEndTime();

        manager.addSubtaskObj(subtask0);
        manager.addSubtaskObj(subtask1);

        ArrayList<Subtask> subtasks = manager.getSubtasks();
        String jsonFormattedTasks = gson.toJson(subtasks);
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        String responseBody = response.body();
        int responseStatus = response.statusCode();

        Assertions.assertEquals(responseStatus, 200);
        Assertions.assertEquals(jsonFormattedTasks, responseBody);
    }

    @Test
    public void getSubtasksWhenListIsEmptyTest() throws IOException, InterruptedException {
        String answer = "Список подзадач пуст.";
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        String responseBody = response.body();
        int responseStatus = response.statusCode();
        Assertions.assertEquals(responseStatus, 404);
        Assertions.assertEquals(answer, responseBody);
    }

    @Test
    public void getSubtaskByIdTest() throws IOException, InterruptedException {
        Subtask subtask0 = new Subtask("subtask0","to do something");
        subtask0.setStartTime(2024, 3, 15, 16, 32);
        subtask0.setDuration(60);
        subtask0.getEndTime();
        manager.addSubtaskObj(subtask0);
        Subtask subtask = manager.getSubtaskById(subtask0.getIdNum());
        String jsonFormattedTask = gson.toJson(subtask);
        URI url = URI.create("http://localhost:8080/subtasks/" + subtask0.getIdNum());
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(jsonFormattedTask, response.body());
    }

    @Test
    public void getSubtaskByIdForWrongNumTest() throws IOException, InterruptedException {
        String answer = "Такой подзадачи нет.";
        URI url = URI.create("http://localhost:8080/subtasks/35");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(response.statusCode(), 404);
        Assertions.assertEquals(answer, response.body());
    }

    @Test
    public void addSubtaskObjTest() throws IOException, InterruptedException {
        Subtask subtask0 = new Subtask("subtask0","to do something");
        subtask0.setStartTime(2024, 3, 15, 16, 32);
        subtask0.setDuration(60);
        subtask0.getEndTime();
        subtask0.setIdNum(2);

        String jsonFormattedTask = gson.toJson(subtask0);

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonFormattedTask))
                .uri(url)
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        ArrayList<Subtask> list = manager.getSubtasks();

        Subtask subtask = manager.getSubtaskById( list.get(0).getIdNum());
        String taksFromManager = gson.toJson(subtask);

        Assertions.assertEquals(response.statusCode(), 201);
        Assertions.assertEquals(jsonFormattedTask, taksFromManager);
    }

    @Test
    public void updSubtaskTest() throws IOException, InterruptedException {
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
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonFormattedTask))
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Subtask subtask = manager.getSubtaskById(3);
        String taksFromManager = gson.toJson(subtask);
        Assertions.assertEquals(response.statusCode(), 201);
        Assertions.assertEquals(jsonFormattedTask, taksFromManager);
    }

    @Test
    public void overlapTest() throws IOException, InterruptedException {
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
                "  \"idNum\": 3,\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"duration\": \"PT1H\",\n" +
                "  \"startTime\": \"2024-03-15T16:32:00\",\n" +
                "  \"endTime\": \"2024-03-15T17:32:00\"\n" +
                "}";
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonFormattedTask0))
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        HttpRequest request1 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonFormattedTask1))
                .uri(url)
                .build();
        HttpResponse<String> response1 = client.send(request1, handler);
        Assertions.assertEquals(response1.statusCode(), 406);
        Assertions.assertEquals(response1.body(), "Добавляемая подзадача переcекается с другой.");
    }

    @Test
    public void deleteSubtaskWithoutNumTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(response.statusCode(), 500);
        Assertions.assertEquals(response.body(), "Не указан номер удаляемой подзадачи.");
    }

    @Test
    public void deleteNonexistentSubaskTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/subtasks/37");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(response.statusCode(), 404);
        Assertions.assertEquals(response.body(), "Удаляемой подзадачи не существует.");
    }

    @Test
    public void deleteSubtaskTest() throws IOException, InterruptedException {
        Subtask subtask0 = new Subtask("subtask0","to do something");
        subtask0.setStartTime(2024, 3, 15, 16, 32);
        subtask0.setDuration(60);
        subtask0.getEndTime();

        Epic epic = new Epic("epic", "desc");

        manager.addSubtaskObj(subtask0);
        manager.addEpicObj(epic);

        subtask0.setEpicId(epic.getIdNum());
        epic.linkSubtaskToEpic(subtask0);

        URI url = URI.create("http://localhost:8080/subtasks/" + subtask0.getIdNum());
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);


        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(response.body(), "Подзадача удалена.");
    }

    @Test
    public void wrongMethodTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/subtasks");
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
}
