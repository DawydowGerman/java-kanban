package ru.practicum.kanban.converters;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.Duration;

public class DurationConverter implements JsonSerializer<Duration>, JsonDeserializer<Duration> {
    @Override
    public JsonElement serialize(Duration src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return null;
        }
        return new JsonPrimitive(src.toString());
    }

    @Override
    public Duration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json == null) {
            return null;
        }
        if (json.isJsonNull()) {
            return null;
        }

        final String zoneIdentifier = json.getAsString();
        if (zoneIdentifier == null || zoneIdentifier.isEmpty()) {
            return null;
        }
        return Duration.parse(json.getAsString());
    }
}
