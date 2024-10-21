package main.kanban1.java.test;

import com.google.gson.Gson;
import main.kanban1.java.src.API.HttpTaskServer;
import main.kanban1.java.src.Interfaces.HistoryManager;
import main.kanban1.java.src.Interfaces.TaskManager;
import main.kanban1.java.src.manager.InMemoryTaskManager;
import main.kanban1.java.src.tasks.Task;
import main.kanban1.java.src.util.Managers;
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
    Managers managers = new Managers();
    HistoryManager historyManager = managers.getDefaultHistory();
    Gson gson = managers.getGson();
    TaskManager manager = new InMemoryTaskManager(historyManager);
    HttpTaskServer taskServer = new HttpTaskServer(manager, gson);

    public HttpHistoryManagerTest() throws IOException {
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
    public void getHistoryWhenHistoryEmptyTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(response.statusCode(), 404);
        Assertions.assertEquals(response.body(), "Список истории пуст.");
    }

    @Test
    public void getHistoryTest() throws IOException, InterruptedException {
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
        manager.getTaskById(task0.getIdNum());
        manager.getTaskById(task1.getIdNum());
        List<Task> tasksList0 = manager.getTasks();
        String tasksListStr0 = gson.toJson(tasksList0);
        List<Task> tasksList1 = manager.getHistory();
        String tasksListStr1 = gson.toJson(tasksList1);
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(tasksListStr0, tasksListStr1);
    }

    @Test
    public void wrongMethodTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/history");
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
