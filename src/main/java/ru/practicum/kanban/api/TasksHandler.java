package ru.practicum.kanban.api;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.practicum.kanban.interfaces.TaskManager;
import ru.practicum.kanban.exceptions.OvelapException;
import ru.practicum.kanban.tasks.Task;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {

    public TasksHandler(TaskManager taskManager, Gson gson) {
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
            Task task = getTaskManager().getTaskById(id);
            if (task == null) {
                sendResponse(httpExchange, 404, "Такой задачи нет.");
                return;
            }
            sendResponse(httpExchange, 200, getGson().toJson(task));
        }
        List<Task> tasks = getTaskManager().getTasks();
        if (tasks.size() == 0) {
            sendResponse(httpExchange, 404, "Список задач пуст.");
        }
        sendResponse(httpExchange, 200, getGson().toJson(tasks));
    }

    @Override
    protected void processPost(String path, HttpExchange httpExchange) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Task task = getGson().fromJson(body,Task.class);
        try {
            if (task.getIdNum() == 0) {
                getTaskManager().addTaskObj(task);
                sendResponse(httpExchange, 201, "Задача добавлена.");
            } else {
                getTaskManager().updTask(task);
                sendResponse(httpExchange, 201, "Задача обновлена.");
            }
        } catch (OvelapException e) {
            sendResponse(httpExchange, 406, "Добавляемая задача переcекается с другой.");
        }
    }

    @Override
    protected void processDelete(String path, HttpExchange httpExchange) throws IOException {
        if (path.length() <= 6) {
            sendResponse(httpExchange, 500, "Не указан номер удаляемой задачи.");
        }
        int taskListSizeBefore = getTaskManager().getTasks().size();
        String idStr = path.split("/")[2];
        int id = Integer.parseInt(idStr);
        getTaskManager().deleteTaskById(id);
        if (taskListSizeBefore == getTaskManager().getTasks().size()) {
            sendResponse(httpExchange, 404, "Удаляемой задачи не существует.");
        }
        sendResponse(httpExchange, 200, "Задача удалена.");
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}