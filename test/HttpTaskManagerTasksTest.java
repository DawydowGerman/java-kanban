package main.kanban1.java.test;

import com.google.gson.Gson;
import main.kanban1.java.src.API.HttpTaskServer;
import main.kanban1.java.src.Interfaces.HistoryManager;
import main.kanban1.java.src.Interfaces.TaskManager;
import main.kanban1.java.src.manager.InMemoryTaskManager;
import main.kanban1.java.src.tasks.Task;
import main.kanban1.java.src.util.Managers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class HttpTaskManagerTasksTest {
    Managers managers = new Managers();
    HistoryManager historyManager = managers.getDefaultHistory();
    Gson gson = managers.getGson();
    TaskManager manager = new InMemoryTaskManager(historyManager);
    HttpTaskServer taskServer = new HttpTaskServer(manager, gson);

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
        Task task0 = new Task("task0","to do something");
        task0.setStartTime(2024, 3, 15, 16, 32);
        task0.setDuration(60);
        task0.getEndTime();
        Task task1 = new Task("task0","to do something");
        task1.setStartTime(2022, 3, 15, 16, 32);
        task1.setDuration(60);
        task1.getEndTime();
        manager.addTaskObj(task0);
        manager.addTaskObj(task1);
        ArrayList<Task> tasks = manager.getTasks();
        String jsonFormattedTasks = gson.toJson(tasks);
        URI url = URI.create("http://localhost:8080/tasks");
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
    public void getTasksWhenListIsEmptyTest() throws IOException, InterruptedException {
        String answer = "Список задач пуст.";
        URI url = URI.create("http://localhost:8080/tasks");
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
    public void getTaskByIdTest() throws IOException, InterruptedException {
        Task task0 = new Task("task0","to do something");
        task0.setStartTime(2024, 3, 15, 16, 32);
        task0.setDuration(60);
        task0.getEndTime();

        manager.addTaskObj(task0);

        Task task = manager.getTaskById(task0.getIdNum());

        String jsonFormattedTask = gson.toJson(task);

        URI url = URI.create("http://localhost:8080/tasks/" + task0.getIdNum());
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
    public void getTaskByIdForWrongNumTest() throws IOException, InterruptedException {
        String answer = "Такой задачи нет.";
        URI url = URI.create("http://localhost:8080/tasks/35");
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
    public void addTaskObjTest() throws IOException, InterruptedException {
        Task task0 = new Task("task0","to do something");
        task0.setStartTime(2024, 3, 15, 16, 32);
        task0.setDuration(60);
        task0.getEndTime();
        task0.setIdNum(3);
        String jsonFormattedTask = gson.toJson(task0);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonFormattedTask))
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        ArrayList<Task> list = manager.getTasks();
        Task task = manager.getTaskById(list.get(0).getIdNum());
        String taksFromManager = gson.toJson(task);
        Assertions.assertEquals(response.statusCode(), 201);
        Assertions.assertEquals(jsonFormattedTask, taksFromManager);
    }

    @Test
    public void updTaskTest() throws IOException, InterruptedException {
        String jsonFormattedTask = "{\n" +
                "  \"name\": \"task0\",\n" +
                "  \"description\": \"to do something\",\n" +
                "  \"idNum\": 3,\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"duration\": \"PT1H\",\n" +
                "  \"startTime\": \"2024-03-15T16:32:00\",\n" +
                "  \"endTime\": \"2024-03-15T17:32:00\"\n" +
                "}";
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonFormattedTask))
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Task task = manager.getTaskById(3);
        String taksFromManager = gson.toJson(task);
        Assertions.assertEquals(response.statusCode(), 201);
        Assertions.assertEquals(jsonFormattedTask, taksFromManager);
    }

    @Test
    public void overlapTest() throws IOException, InterruptedException {
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
                "  \"idNum\": 3,\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"duration\": \"PT1H\",\n" +
                "  \"startTime\": \"2024-03-15T16:32:00\",\n" +
                "  \"endTime\": \"2024-03-15T17:32:00\"\n" +
                "}";
        URI url = URI.create("http://localhost:8080/tasks");
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
        Assertions.assertEquals(response1.body(), "Добавляемая задача переcекается с другой.");
    }

    @Test
    public void deleteTaskWithoutNumTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(response.statusCode(), 500);
        Assertions.assertEquals(response.body(), "Не указан номер удаляемой задачи.");
    }

    @Test
    public void deleteNonexistentTaskTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/37");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(response.statusCode(), 404);
        Assertions.assertEquals(response.body(), "Удаляемой задачи не существует.");
    }

    @Test
    public void deleteTaskTest() throws IOException, InterruptedException {
        Task task0 = new Task("task0","to do something");
        task0.setStartTime(2024, 3, 15, 16, 32);
        task0.setDuration(60);
        task0.getEndTime();

        manager.addTaskObj(task0);

        URI url = URI.create("http://localhost:8080/tasks/" + task0.getIdNum());
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(response.body(), "Задача удалена.");
    }

    @Test
    public void wrongMethodTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks");
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
