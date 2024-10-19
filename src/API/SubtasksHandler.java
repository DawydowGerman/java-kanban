package main.kanban1.java.src.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.kanban1.java.src.Interfaces.TaskManager;
import main.kanban1.java.src.tasks.Subtask;

import main.kanban1.java.src.exceptions.OvelapException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class SubtasksHandler implements HttpHandler {
    private TaskManager taskManager;
    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Duration.class, new DurationConverter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter())
            .create();

    public SubtasksHandler(TaskManager taskManager) {
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
                Subtask subtask = taskManager.getSubtaskById(id);
                    if (subtask == null) {
                        String response = "Такой подзадачи нет.";
                        httpExchange.sendResponseHeaders(404, 0);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    }
                String response = gson.toJson(subtask);
                httpExchange.sendResponseHeaders(200, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
            ArrayList<Subtask> subtasks = taskManager.getSubtasks();
                if (subtasks.size() == 0) {
                    String response = "Список подзадач пуст.";
                    httpExchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
            String response = gson.toJson(subtasks);
            httpExchange.sendResponseHeaders(200, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } else if (method.equals("POST")) {
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Subtask subtask = gson.fromJson(body,Subtask.class);
            try {
                if (subtask.getIdNum() == 0) {
                    taskManager.addSubtaskObj(subtask);
                    String response = "Подзадача добавлена.";
                    httpExchange.sendResponseHeaders(201, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                } else {
                    taskManager.updSubtask(subtask);
                    String response = "Подзадача обновлена.";
                    httpExchange.sendResponseHeaders(201, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
            } catch (OvelapException e) {
                String response = "Добавляемая подзадача переcекается с другой.";
                httpExchange.sendResponseHeaders(406, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        } else if (method.equals("DELETE")) {
            if (path.length() <= 9) {
                String response = "Не указан номер удаляемой подзадачи.";
                httpExchange.sendResponseHeaders(500, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
            int subtaskListSizeBefore = taskManager.getSubtasks().size();
            String idStr = path.split("/")[2];
            int id = Integer.parseInt(idStr);
            taskManager.deleteSubtaskById(id);
                if (subtaskListSizeBefore == taskManager.getSubtasks().size()) {
                    String response = "Удаляемой подзадачи не существует.";
                    httpExchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
            String response = "Подзадача удалена.";
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
