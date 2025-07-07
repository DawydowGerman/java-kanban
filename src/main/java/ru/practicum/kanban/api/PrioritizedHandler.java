package ru.practicum.kanban.api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.practicum.kanban.interfaces.TaskManager;
import ru.practicum.kanban.tasks.Task;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    public PrioritizedHandler(TaskManager taskManager, Gson gson) {
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
        List<Task> prioritizedTasksList = getTaskManager().getPrioritizedTasks();
        if (prioritizedTasksList.size() == 0) {
            sendResponse(httpExchange, 404, "Список истории пуст.");
            return;
        }
        sendResponse(httpExchange, 200, getGson().toJson(prioritizedTasksList));
    }

    @Override
    protected void processPost(String path, HttpExchange httpExchange) throws IOException {
        sendResponse(httpExchange, 500, "Некорректный метод!");
    }

    @Override
    protected void processDelete(String path, HttpExchange httpExchange) throws IOException {
        sendResponse(httpExchange, 500, "Некорректный метод!");
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}