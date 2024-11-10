package de.emilschlampp.scheCPU.util;

public class ContentSizeException extends ContentInvalidException {
    public ContentSizeException() {
    }

    public ContentSizeException(String message) {
        super(message);
    }

    public ContentSizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContentSizeException(Throwable cause) {
        super(cause);
    }

    public ContentSizeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
