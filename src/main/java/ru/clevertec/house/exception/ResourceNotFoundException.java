package ru.clevertec.house.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Throwable ex) {
        super(ex);
    }
}
