package de.emilschlampp.scheCPU.util;

public class ContentInvalidException extends RuntimeException {
    public ContentInvalidException() {
    }

    public ContentInvalidException(String message) {
        super(message);
    }

    public ContentInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContentInvalidException(Throwable cause) {
        super(cause);
    }

    public ContentInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
