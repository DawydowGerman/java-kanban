package main.kanban1.java.src.API;

import com.google.gson.Gson;
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
                String response = "Такой подзадачи нет.";
                httpExchange.sendResponseHeaders(404, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
            String response = getGson().toJson(subtask);
            httpExchange.sendResponseHeaders(200, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
        ArrayList<Subtask> subtasks = getTaskManager().getSubtasks();
        if (subtasks.size() == 0) {
            String response = "Список подзадач пуст.";
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

    @Override
    protected void processPost(String path, HttpExchange httpExchange) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Subtask subtask = getGson().fromJson(body,Subtask.class);
        try {
            if (subtask.getIdNum() == 0) {
                getTaskManager().addSubtaskObj(subtask);
                String response = "Подзадача добавлена.";
                httpExchange.sendResponseHeaders(201, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                getTaskManager().updSubtask(subtask);
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
    }

    @Override
    protected void processDelete(String path, HttpExchange httpExchange) throws IOException {
        if (path.length() <= 9) {
            String response = "Не указан номер удаляемой подзадачи.";
            httpExchange.sendResponseHeaders(500, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
        int subtaskListSizeBefore = getTaskManager().getSubtasks().size();
        String idStr = path.split("/")[2];
        int id = Integer.parseInt(idStr);
        getTaskManager().deleteSubtaskById(id);
        if (subtaskListSizeBefore == getTaskManager().getSubtasks().size()) {
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
    }
}
