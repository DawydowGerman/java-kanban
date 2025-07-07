package ru.practicum.kanban.exceptions;

public class OvelapException extends RuntimeException {
    public OvelapException() {
    }

    public OvelapException(String message) {
        super(message);
    }

    public OvelapException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public OvelapException(final Throwable cause) {
        super(cause);
    }
}