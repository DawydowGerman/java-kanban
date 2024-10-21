package main.kanban1.java.src.API;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.kanban1.java.src.Interfaces.TaskManager;
import main.kanban1.java.src.tasks.Epic;
import main.kanban1.java.src.tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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
                String response = "Такого эпика нет.";
                httpExchange.sendResponseHeaders(404, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
            String response = getGson().toJson(epic);
            httpExchange.sendResponseHeaders(200, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } else if (numOfSlashes == 3) {
            if (path.split("/")[3].equals("subtasks")) {
                String idStr = path.split("/")[2];
                int id = Integer.parseInt(idStr);
                Epic epic = getTaskManager().getEpicById(id);
                if (epic == null) {
                    String response = "Такого эпика нет.";
                    httpExchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
                ArrayList<Subtask> subtasks = getTaskManager().getAllSubtasksOfOneEpic(epic);
                if (subtasks.size() == 0) {
                    String response = "Список эпиков пуст.";
                    httpExchange.sendResponseHeaders(404, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
                String response = getGson().toJson(subtasks);
                httpExchange.sendResponseHeaders(200, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        } else {
            ArrayList<Epic> epics = getTaskManager().getEpics();
            if (epics.size() == 0) {
                String response = "Список эпиков пуст.";
                httpExchange.sendResponseHeaders(404, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
            String response = getGson().toJson(epics);
            httpExchange.sendResponseHeaders(200, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    @Override
    protected void processPost(String path, HttpExchange httpExchange) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = getGson().fromJson(body, Epic.class);
        getTaskManager().addEpicObj(epic);
        String response = "Эпик добавлен.";
        httpExchange.sendResponseHeaders(201, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    @Override
    protected void processDelete(String path, HttpExchange httpExchange) throws IOException {
        if (path.length() <= 6) {
            String response = "Не указан номер удаляемого эпика.";
            httpExchange.sendResponseHeaders(500, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
        int epicsListSizeBefore = getTaskManager().getEpics().size();
        String idStr = path.split("/")[2];
        int id = Integer.parseInt(idStr);
        getTaskManager().deleteEpicById(id);
        if (epicsListSizeBefore == getTaskManager().getTasks().size()) {
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
    }
}
