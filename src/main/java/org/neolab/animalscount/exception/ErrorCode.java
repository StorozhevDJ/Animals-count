package org.neolab.animalscount.exception;

public enum ErrorCode {
    DB_DRIVER_NOT_CONNECTED("H2 DB driver NOT connected!"),
    DB_NOT_CONNECTED("DB NOT connected!"),
    DB_IMPORT_FAILED("DB import from file failed!"),
    DB_GET_COUNT_FAILED("DB calculate animal counter failed!"),
    OTHER_ERROR("Undefined error!");


    private String message;


    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
