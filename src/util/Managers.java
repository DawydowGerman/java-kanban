package main.kanban1.java.src.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.kanban1.java.src.Interfaces.HistoryManager;
import main.kanban1.java.src.manager.InMemoryHistoryManager;
import main.kanban1.java.src.manager.InMemoryTaskManager;
import main.kanban1.java.src.Interfaces.TaskManager;
import java.time.Duration;
import java.time.LocalDateTime;
import main.kanban1.java.src.Converters.DurationConverter;
import main.kanban1.java.src.Converters.LocalDateTimeConverter;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        return new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Duration.class, new DurationConverter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter())
            .create();
    }
}
