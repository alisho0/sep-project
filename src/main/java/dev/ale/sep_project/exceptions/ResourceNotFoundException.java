package dev.ale.sep_project.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, Long id) {
        super(String.format("%s con id %d no encontrado", resource, id));
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}