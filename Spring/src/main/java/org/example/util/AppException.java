package org.example.util;

public class AppException extends RuntimeException {

    private String message;

    private AppException() {
        super();
    }

    public AppException(String message, Object... args) {
        super(String.format(message, args));
    }

    public AppException(String message, Throwable exception) {
        super(message, exception);
    }

    public static AppException create(String message, Object... args) {
        return new AppException(message, args);
    }

    public static AppException create(String message, Throwable exception) {
        return new AppException(message, exception);
    }
}
