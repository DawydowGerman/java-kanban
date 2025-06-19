package ru.practicum.kanban.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.practicum.kanban.interfaces.HistoryManager;
import ru.practicum.kanban.manager.InMemoryHistoryManager;
import ru.practicum.kanban.manager.InMemoryTaskManager;
import ru.practicum.kanban.interfaces.TaskManager;
import java.time.Duration;
import java.time.LocalDateTime;
import ru.practicum.kanban.converters.DurationConverter;
import ru.practicum.kanban.converters.LocalDateTimeConverter;

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