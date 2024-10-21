package main.kanban1.java.src.API;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.kanban1.java.src.Interfaces.TaskManager;
import main.kanban1.java.src.exceptions.OvelapException;
import main.kanban1.java.src.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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
                String response = "Такой задачи нет.";
                httpExchange.sendResponseHeaders(404, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
            String response = getGson().toJson(task);
            httpExchange.sendResponseHeaders(200, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
        ArrayList<Task> tasks = getTaskManager().getTasks();
        if (tasks.size() == 0) {
            String response = "Список задач пуст.";
            httpExchange.sendResponseHeaders(404, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
        String response = getGson().toJson(tasks);
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    @Override
    protected void processPost(String path, HttpExchange httpExchange) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Task task = getGson().fromJson(body,Task.class);
        try {
            if (task.getIdNum() == 0) {
                getTaskManager().addTaskObj(task);
                String response = "Задача добавлена.";
                httpExchange.sendResponseHeaders(201, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                getTaskManager().updTask(task);
                String response = "Задача обновлена.";
                httpExchange.sendResponseHeaders(201, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        } catch (OvelapException e) {
            String response = "Добавляемая задача переcекается с другой.";
            httpExchange.sendResponseHeaders(406, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    @Override
    protected void processDelete(String path, HttpExchange httpExchange) throws IOException {
        if (path.length() <= 6) {
            String response = "Не указан номер удаляемой задачи.";
            httpExchange.sendResponseHeaders(500, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
        int taskListSizeBefore = getTaskManager().getTasks().size();
        String idStr = path.split("/")[2];
        int id = Integer.parseInt(idStr);
        getTaskManager().deleteTaskById(id);
        if (taskListSizeBefore == getTaskManager().getTasks().size()) {
            String response = "Удаляемой задачи не существует.";
            httpExchange.sendResponseHeaders(404, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
        String response = "Задача удалена.";
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
