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
            String response = "Список истории пуст.";
            httpExchange.sendResponseHeaders(404, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
        String response = getGson().toJson(prioritizedTasksList);
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    @Override
    protected void processPost(String path, HttpExchange httpExchange) throws IOException {
        String response = "Некорректный метод!";
        httpExchange.sendResponseHeaders(500, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    @Override
    protected void processDelete(String path, HttpExchange httpExchange) throws IOException {
        String response = "Некорректный метод!";
        httpExchange.sendResponseHeaders(500, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
