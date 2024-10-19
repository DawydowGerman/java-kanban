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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TasksHandler implements HttpHandler {
    private TaskManager taskManager;
    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Duration.class, new DurationConverter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter())
            .create();

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        if (method.equals("GET")) {
            long numOfSlashes = path.chars().filter(ch -> ch == '/').count();
            if (numOfSlashes == 2) {
                String idStr = path.split("/")[2];
                int id = Integer.parseInt(idStr);
                Task task = taskManager.getTaskById(id);
                    if (task == null) {
                        String response = "Такой задачи нет.";
                        httpExchange.sendResponseHeaders(404, 0);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    }
                String response = gson.toJson(task);
                httpExchange.sendResponseHeaders(200, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
            ArrayList<Task> tasks = taskManager.getTasks();
                if (tasks.size() == 0) {
                    String response = "Список задач пуст.";
                    httpExchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
            String response = gson.toJson(tasks);
            httpExchange.sendResponseHeaders(200, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } else if (method.equals("POST")) {
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Task task = gson.fromJson(body,Task.class);
            try {
                if (task.getIdNum() == 0) {
                    taskManager.addTaskObj(task);
                    String response = "Задача добавлена.";
                    httpExchange.sendResponseHeaders(201, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                } else {
                    taskManager.updTask(task);
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
        } else if (method.equals("DELETE")) {
            if (path.length() <= 6) {
                String response = "Не указан номер удаляемой задачи.";
                httpExchange.sendResponseHeaders(500, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
            int taskListSizeBefore = taskManager.getTasks().size();
            String idStr = path.split("/")[2];
            int id = Integer.parseInt(idStr);
            taskManager.deleteTaskById(id);
                if (taskListSizeBefore == taskManager.getTasks().size()) {
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
        } else {
            String response = "Некорректный метод!";
            httpExchange.sendResponseHeaders(500, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
