package ru.practicum.kanban;

import com.google.gson.Gson;
import ru.practicum.kanban.api.HttpTaskServer;
import ru.practicum.kanban.interfaces.HistoryManager;
import ru.practicum.kanban.interfaces.TaskManager;
import ru.practicum.kanban.manager.InMemoryTaskManager;
import ru.practicum.kanban.tasks.Epic;
import ru.practicum.kanban.tasks.Subtask;
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

public class HttpEpicManagerTasksTest {
    private static final String BASE_URL = "http://localhost:8080/epics";
    private static final String EPIC_NOT_FOUND_MESSAGE = "Такого эпика нет.";
    private static final String EMPTY_EPIC_LIST_MESSAGE = "Список эпиков пуст.";
    private static final String DELETE_SUCCESS_MESSAGE = "Эпик удален.";
    private static final String WRONG_ID_ERROR_MESSAGE = "Не указан номер удаляемого эпика.";
    private static final String WRONG_METHOD_MESSAGE = "Некорректный метод!";
    private static final int NON_EXISTENT_ID = 999;

    private final Managers managers = new Managers();
    private final HistoryManager historyManager = managers.getDefaultHistory();
    private final Gson gson = managers.getGson();
    private final TaskManager manager = new InMemoryTaskManager(historyManager);
    private final HttpTaskServer taskServer = new HttpTaskServer(manager, gson);

    public HttpEpicManagerTasksTest() throws IOException {
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
    void getEpicsShouldReturnAllEpics() throws Exception {
        Epic epic0 = new Epic("epic0", "desc");
        Epic epic1 = new Epic("epic1", "desc");
        manager.addEpicObj(epic0);
        manager.addEpicObj(epic1);
        HttpResponse<String> response = sendGetRequest(BASE_URL);
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(gson.toJson(manager.getEpics()), response.body());
    }

    @Test
    void getEpicsWhenListIsEmptyTest() throws Exception {
        HttpResponse<String> response = sendGetRequest(BASE_URL);
        Assertions.assertEquals(404, response.statusCode());
        Assertions.assertEquals(EMPTY_EPIC_LIST_MESSAGE, response.body());
    }

    @Test
    void getEpicByIdTest() throws Exception {
        Epic epic0 = new Epic("epic", "desc");
        manager.addEpicObj(epic0);
        Epic expectedEpic = manager.getEpicById(epic0.getIdNum());
        HttpResponse<String> response = sendGetRequest(BASE_URL + "/" + epic0.getIdNum());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(gson.toJson(expectedEpic), response.body());
    }

    @Test
    void getEpicByIdForWrongNumTest() throws Exception {
        HttpResponse<String> response = sendGetRequest(BASE_URL + "/" + NON_EXISTENT_ID);
        Assertions.assertEquals(404, response.statusCode());
        Assertions.assertEquals(EPIC_NOT_FOUND_MESSAGE, response.body());
    }

    @Test
    void getAllSubtasksOfOneEpicTest() throws Exception {
        Epic epic0 = new Epic("epic", "desc");
        Subtask subtask0 = createTestSubtask("subtask0");
        Subtask subtask1 = new Subtask(60, 2020, 3, 15, 16, 32);

        manager.addEpicObj(epic0);
        manager.addSubtaskObj(subtask0);
        manager.addSubtaskObj(subtask1);
        linkSubtaskToEpic(subtask0, epic0);
        linkSubtaskToEpic(subtask1, epic0);

        HttpResponse<String> response = sendGetRequest(BASE_URL + "/" + epic0.getIdNum() + "/subtasks");

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(gson.toJson(manager.getSubtasks()), response.body());
    }

    @Test
    void getAllSubtasksOfEpicShouldReturn404ForNonExistentEpic() throws Exception {
        HttpResponse<String> response = sendGetRequest(BASE_URL + "/" + NON_EXISTENT_ID + "/subtasks");
        Assertions.assertEquals(404, response.statusCode());
        Assertions.assertEquals(EPIC_NOT_FOUND_MESSAGE, response.body());
    }

    @Test
    void addEpicObjTest() throws Exception {
        Epic epic0 = new Epic("epic", "desc");
        String serializedEpic = gson.toJson(epic0);

        HttpResponse<String> response = sendPostRequest(BASE_URL, serializedEpic);
        List<Epic> epics = manager.getEpics();
        Epic createdEpic = manager.getEpicById(epics.get(0).getIdNum());

        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertEquals(1, epics.size());
        Assertions.assertEquals(epic0.getName(), createdEpic.getName());
        Assertions.assertEquals(epic0.getDescription(), createdEpic.getDescription());
    }

    @Test
    void deleteEpicWithoutNumTest() throws Exception {
        HttpResponse<String> response = sendDeleteRequest(BASE_URL);

        Assertions.assertEquals(500, response.statusCode());
        Assertions.assertEquals(WRONG_ID_ERROR_MESSAGE, response.body());
    }

    @Test
    void deleteNonexistentEpicTest() throws Exception {
        HttpResponse<String> response = sendDeleteRequest(BASE_URL + "/" + NON_EXISTENT_ID);

        Assertions.assertEquals(404, response.statusCode());
        Assertions.assertEquals("Удаляемого эпика не существует.", response.body());
    }

    @Test
    void deleteEpicTest() throws Exception {
        Epic epic = new Epic("epic", "desc");
        Subtask subtask0 = createTestSubtask("subtask0");

        manager.addEpicObj(epic);
        manager.addSubtaskObj(subtask0);
        linkSubtaskToEpic(subtask0, epic);

        HttpResponse<String> response = sendDeleteRequest(BASE_URL + "/" + epic.getIdNum());

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(DELETE_SUCCESS_MESSAGE, response.body());
        Assertions.assertEquals(0, manager.getEpics().size());
    }

    @Test
    void wrongMethodTest() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(""))
                .uri(URI.create(BASE_URL))
                .build();

        HttpResponse<String> response = sendRequest(request);

        Assertions.assertEquals(500, response.statusCode());
        Assertions.assertEquals(WRONG_METHOD_MESSAGE, response.body());
    }

    private Subtask createTestSubtask(String name) {
        Subtask subtask = new Subtask(name, "to do something");
        subtask.setStartTime(2024, 3, 15, 16, 32);
        subtask.setDuration(60);
        subtask.setEndTime(subtask.getStartTime().plus(subtask.getDuration()));
        return subtask;
    }

    private void linkSubtaskToEpic(Subtask subtask, Epic epic) {
        subtask.setEpicId(epic.getIdNum());
        epic.linkSubtaskToEpic(subtask);
    }

    private HttpResponse<String> sendGetRequest(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();
        return sendRequest(request);
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
        HttpClient client = HttpClient.newHttpClient();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}