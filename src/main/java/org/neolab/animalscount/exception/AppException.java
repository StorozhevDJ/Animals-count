package org.neolab.animalscount.exception;

public class AppException extends Exception {

    private static final long serialVersionUID = 7825341407651728662L;

    private final ErrorCode error;
    private final String message;

    public AppException(ErrorCode error) {
        this(error, null);
    }

    public AppException(ErrorCode error, String message) {
        this.message = message;
        this.error = error;
    }

    public ErrorCode getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

}
