package ru.clevertec.house.exception;

public class SqlExecuteException extends RuntimeException {

    public SqlExecuteException(Throwable ex) {
        super(ex);
    }
}