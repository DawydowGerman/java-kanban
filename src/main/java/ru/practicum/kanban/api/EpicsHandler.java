package ru.practicum.kanban.api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.practicum.kanban.interfaces.TaskManager;
import ru.practicum.kanban.tasks.Epic;
import ru.practicum.kanban.tasks.Subtask;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    public EpicsHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public TaskManager getTaskManager() {
        return super.getTaskManager();
    }

    @Override
    public Gson getGson() {
        return super.getGson();
    }

    @Override
    protected void processGet(String path, HttpExchange httpExchange) throws IOException {
        long numOfSlashes = path.chars().filter(ch -> ch == '/').count();
        if (numOfSlashes == 2) {
            String idStr = path.split("/")[2];
            int id = Integer.parseInt(idStr);
            Epic epic = getTaskManager().getEpicById(id);
            if (epic == null) {
                sendResponse(httpExchange, 404, "Такого эпика нет.");
                return;
            }
            String response = getGson().toJson(epic);
            sendResponse(httpExchange, 200, response);
        } else if (numOfSlashes == 3 && path.split("/")[3].equals("subtasks")) {
            String idStr = path.split("/")[2];
            int id = Integer.parseInt(idStr);
            Epic epic = getTaskManager().getEpicById(id);
            if (epic == null) {
                sendResponse(httpExchange, 404, "Такого эпика нет.");
                return;
            }
            List<Subtask> subtasks = getTaskManager().getAllSubtasksOfOneEpic(epic);
            if (subtasks.size() == 0) {
                sendResponse(httpExchange, 404, "Список эпиков пуст.");
                return;
            }
            String response = getGson().toJson(subtasks);
            sendResponse(httpExchange, 200, response);
        } else if (numOfSlashes == 1) {
            List<Epic> epics = getTaskManager().getEpics();
            if (epics.size() == 0) {
                sendResponse(httpExchange, 404, "Список эпиков пуст.");
                return;
            }
            String response = getGson().toJson(epics);
            sendResponse(httpExchange, 200, response);
        } else {
            sendResponse(httpExchange, 400, "Некорректный путь.");
        }
    }

    @Override
    protected void processPost(String path, HttpExchange httpExchange) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = getGson().fromJson(body, Epic.class);
        getTaskManager().addEpicObj(epic);
        sendResponse(httpExchange, 201, "Эпик добавлен.");
    }

    @Override
    protected void processDelete(String path, HttpExchange httpExchange) throws IOException {
        if (path.length() <= 6) {
            sendResponse(httpExchange, 500, "Не указан номер удаляемого эпика.");
            return;
        }
        int epicsListSizeBefore = getTaskManager().getEpics().size();
        String idStr = path.split("/")[2];
        int id = Integer.parseInt(idStr);
        getTaskManager().deleteEpicById(id);
        if (epicsListSizeBefore == getTaskManager().getTasks().size()) {
            sendResponse(httpExchange, 404, "Удаляемого эпика не существует.");
            return;
        }
        sendResponse(httpExchange, 200, "Эпик удален.");
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}