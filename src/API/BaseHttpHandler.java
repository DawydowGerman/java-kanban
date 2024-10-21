package main.kanban1.java.src.API;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.kanban1.java.src.Interfaces.TaskManager;

import java.io.IOException;
import java.io.OutputStream;

public class BaseHttpHandler implements HttpHandler {
    private TaskManager taskManager;
    private Gson gson;

    public BaseHttpHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public Gson getGson() {
        return gson;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();

        switch (method) {
            case "GET":
                processGet(path, httpExchange);
                break;

            case "POST":
                processPost(path, httpExchange);
                break;

            case "DELETE":
                processDelete(path, httpExchange);
                break;

            default:
                String response = "Некорректный метод!";
                httpExchange.sendResponseHeaders(500, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
                break;
        }
    }

    protected void processGet(String path, HttpExchange httpExchange) throws IOException {
    }

    protected void processPost(String path, HttpExchange httpExchange) throws IOException {
    }

    protected void processDelete(String path, HttpExchange httpExchange) throws IOException {
    }
}
