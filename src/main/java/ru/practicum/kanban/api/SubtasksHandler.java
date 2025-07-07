package ru.practicum.kanban.api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.practicum.kanban.interfaces.TaskManager;
import ru.practicum.kanban.tasks.Subtask;
import ru.practicum.kanban.exceptions.OvelapException;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {

    public SubtasksHandler(TaskManager taskManager, Gson gson) {
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
            Subtask subtask = getTaskManager().getSubtaskById(id);
            if (subtask == null) {
                sendResponse(httpExchange, 404, "Такой подзадачи нет.");
                return;
            }
            sendResponse(httpExchange, 200, getGson().toJson(subtask));
        }
        List<Subtask> subtasks = getTaskManager().getSubtasks();
        if (subtasks.size() == 0) {
            sendResponse(httpExchange, 404, "Список подзадач пуст.");
            return;
        }
        sendResponse(httpExchange, 200, getGson().toJson(subtasks));
    }

    @Override
    protected void processPost(String path, HttpExchange httpExchange) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Subtask subtask = getGson().fromJson(body,Subtask.class);
        try {
            if (subtask.getIdNum() == null) {
                getTaskManager().addSubtaskObj(subtask);
                sendResponse(httpExchange, 201, "Подзадача добавлена.");
            } else {
                getTaskManager().updSubtask(subtask);
                sendResponse(httpExchange, 201, "Подзадача обновлена.");
            }
        } catch (OvelapException e) {
            sendResponse(httpExchange, 406, "Добавляемая подзадача переcекается с другой.");
        }
    }

    @Override
    protected void processDelete(String path, HttpExchange httpExchange) throws IOException {
        if (path.length() <= 9) {
            sendResponse(httpExchange, 500, "Не указан номер удаляемой подзадачи.");
        }
        int subtaskListSizeBefore = getTaskManager().getSubtasks().size();
        String idStr = path.split("/")[2];
        int id = Integer.parseInt(idStr);
        getTaskManager().deleteSubtaskById(id);
        if (subtaskListSizeBefore == getTaskManager().getSubtasks().size()) {
            sendResponse(httpExchange, 404, "Удаляемой подзадачи не существует.");
        }
        sendResponse(httpExchange, 200, "Подзадача удалена.");
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}