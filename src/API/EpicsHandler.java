package main.kanban1.java.src.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.kanban1.java.src.Interfaces.TaskManager;
import main.kanban1.java.src.tasks.Epic;
import main.kanban1.java.src.tasks.Subtask;
import main.kanban1.java.src.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class EpicsHandler implements HttpHandler {
    private TaskManager taskManager;
    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Duration.class, new DurationConverter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter())
            .create();

    public EpicsHandler(TaskManager taskManager) {
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
                Epic epic = taskManager.getEpicById(id);
                if (epic == null) {
                    String response = "Такого эпика нет.";
                    httpExchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
                String response = gson.toJson(epic);
                httpExchange.sendResponseHeaders(200, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else if (numOfSlashes == 3) {
                if (path.split("/")[3].equals("subtasks")) {
                    String idStr = path.split("/")[2];
                    int id = Integer.parseInt(idStr);
                    Epic epic = taskManager.getEpicById(id);
                    if (epic == null) {
                        String response = "Такого эпика нет.";
                        httpExchange.sendResponseHeaders(404, 0);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    }
                    ArrayList<Subtask> subtasks = taskManager.getAllSubtasksOfOneEpic(epic);
                    if (subtasks.size() == 0) {
                        String response = "Список эпиков пуст.";
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
                }
            } else {
                ArrayList<Epic> epics = taskManager.getEpics();
                if (epics.size() == 0) {
                    String response = "Список эпиков пуст.";
                    httpExchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
                String response = gson.toJson(epics);
                httpExchange.sendResponseHeaders(200, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        } else if (method.equals("POST")) {
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Epic epic = gson.fromJson(body,Epic.class);
            taskManager.addEpicObj(epic);
            String response = "Эпик добавлен.";
            httpExchange.sendResponseHeaders(201, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } else if (method.equals("DELETE")) {
            if (path.length() <= 6) {
                String response = "Не указан номер удаляемого эпика.";
                httpExchange.sendResponseHeaders(500, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
            int epicsListSizeBefore = taskManager.getEpics().size();
            String idStr = path.split("/")[2];
            int id = Integer.parseInt(idStr);
            taskManager.deleteEpicById(id);
                if (epicsListSizeBefore == taskManager.getTasks().size()) {
                    String response = "Удаляемого эпика не существует.";
                    httpExchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
            String response = "Эпик удален.";
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
