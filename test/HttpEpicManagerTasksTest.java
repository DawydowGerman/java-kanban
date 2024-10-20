package main.kanban1.java.test;

import com.google.gson.Gson;
import main.kanban1.java.src.API.HttpTaskServer;
import main.kanban1.java.src.Interfaces.HistoryManager;
import main.kanban1.java.src.Interfaces.TaskManager;
import main.kanban1.java.src.manager.InMemoryTaskManager;
import main.kanban1.java.src.tasks.Epic;
import main.kanban1.java.src.tasks.Subtask;
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
import java.util.ArrayList;
import java.util.List;

public class HttpEpicManagerTasksTest {
    Managers managers = new Managers();
    HistoryManager historyManager = managers.getDefaultHistory();
    Gson gson = managers.getGson();
    TaskManager manager = new InMemoryTaskManager(historyManager);
    HttpTaskServer taskServer = new HttpTaskServer(manager, gson);

    public HttpEpicManagerTasksTest() throws IOException {
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
    public void getEpicsTest() throws IOException, InterruptedException {
        Epic epic0 = new Epic("epic0", "desc");
        Epic epic1 = new Epic("epic1", "desc");
        manager.addEpicObj(epic0);
        manager.addEpicObj(epic1);
        ArrayList<Epic> epics = manager.getEpics();
        String jsonFormattedEpics = gson.toJson(epics);
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(jsonFormattedEpics, response.body());
    }

    @Test
    public void getEpicsWhenListIsEmptyTest() throws IOException, InterruptedException {
        String answer = "Список эпиков пуст.";
        URI url = URI.create("http://localhost:8080/epics");
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
    public void getEpicByIdTest() throws IOException, InterruptedException {
        Epic epic0 = new Epic("epic", "desc");
        manager.addEpicObj(epic0);
        Epic epic = manager.getEpicById(epic0.getIdNum());
        String jsonFormattedEpic = gson.toJson(epic);
        URI url = URI.create("http://localhost:8080/epics/" + epic0.getIdNum());
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(jsonFormattedEpic, response.body());
    }

    @Test
    public void getEpicByIdForWrongNumTest() throws IOException, InterruptedException {
        String answer = "Такого эпика нет.";
        URI url = URI.create("http://localhost:8080/epics/35");
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
    public void getAllSubtasksOfOneEpicTest() throws IOException, InterruptedException {
        Subtask subtask0 = new Subtask("subtask0","to do something");
        subtask0.setStartTime(2024, 3, 15, 16, 32);
        subtask0.setDuration(60);
        subtask0.getEndTime();
        Subtask subtask1 = new Subtask("subtask1","to do something");
        subtask1.setStartTime(2000, 3, 15, 16, 32);
        subtask1.setDuration(60);
        subtask1.getEndTime();
        Epic epic0 = new Epic("epic", "desc");
        manager.addEpicObj(epic0);
        manager.addSubtaskObj(subtask0);
        manager.addSubtaskObj(subtask1);
        subtask0.setEpicId(epic0.getIdNum());
        subtask1.setEpicId(epic0.getIdNum());
        epic0.linkSubtaskToEpic(subtask0);
        epic0.linkSubtaskToEpic(subtask1);
        List<Subtask> subsList = manager.getSubtasks();
        String jsonFormattedSubtask = gson.toJson(subsList);
        URI url = URI.create("http://localhost:8080/epics/" + epic0.getIdNum() + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(jsonFormattedSubtask, response.body());
    }

    @Test
    public void getAllSubtasksOfOneEpicWhenNoEpicInManagerTest() throws IOException, InterruptedException {
        Subtask subtask0 = new Subtask("subtask0","to do something");
        subtask0.setStartTime(2024, 3, 15, 16, 32);
        subtask0.setDuration(60);
        subtask0.getEndTime();
        Subtask subtask1 = new Subtask("subtask1","to do something");
        subtask1.setStartTime(2000, 3, 15, 16, 32);
        subtask1.setDuration(60);
        subtask1.getEndTime();
        Epic epic0 = new Epic("epic", "desc");
        manager.addEpicObj(epic0);
        manager.addSubtaskObj(subtask0);
        manager.addSubtaskObj(subtask1);
        subtask0.setEpicId(epic0.getIdNum());
        subtask1.setEpicId(epic0.getIdNum());
        epic0.linkSubtaskToEpic(subtask0);
        epic0.linkSubtaskToEpic(subtask1);
        List<Subtask> subsList = manager.getSubtasks();
        String jsonFormattedSubtask = gson.toJson(subsList);
        URI url = URI.create("http://localhost:8080/epics/10/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(response.statusCode(), 404);
        Assertions.assertEquals("Такого эпика нет.", response.body());
    }

    @Test
    public void addEpicObjTest() throws IOException, InterruptedException {
        Epic epic0 = new Epic("epic", "desc");
        String serialized0 = gson.toJson(epic0);
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(serialized0))
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        ArrayList<Epic> list = manager.getEpics();
        Epic epic = manager.getEpicById(list.get(0).getIdNum());
        epic0.setIdNum(epic.getIdNum());
        String serialized1 = gson.toJson(epic0);
        String taksFromManager = gson.toJson(epic);
        Assertions.assertEquals(response.statusCode(), 201);
        Assertions.assertEquals(serialized1, taksFromManager);
    }

    @Test
    public void deleteEpicWithoutNumTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(response.statusCode(), 500);
        Assertions.assertEquals(response.body(), "Не указан номер удаляемого эпика.");
    }

    @Test
    public void deleteNonexistentEpicTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/epics/37");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(response.statusCode(), 404);
        Assertions.assertEquals(response.body(), "Удаляемого эпика не существует.");
    }

    @Test
    public void deleteEpicTest() throws IOException, InterruptedException {
        Subtask subtask0 = new Subtask("subtask0","to do something");
        subtask0.setStartTime(2024, 3, 15, 16, 32);
        subtask0.setDuration(60);
        subtask0.getEndTime();
        Epic epic = new Epic("epic", "desc");
        manager.addSubtaskObj(subtask0);
        manager.addEpicObj(epic);
        subtask0.setEpicId(epic.getIdNum());
        epic.linkSubtaskToEpic(subtask0);
        URI url = URI.create("http://localhost:8080/epics/11");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(response.body(), "Эпик удален.");
    }

    @Test
    public void wrongMethodTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/epics");
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
