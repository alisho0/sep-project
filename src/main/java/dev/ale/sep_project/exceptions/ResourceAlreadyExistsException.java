package dev.ale.sep_project.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String resource, Long id) {
        super(String.format("%s con id %d ya existe en el sistema", resource, id));
    }

    public ResourceAlreadyExistsException(String resource) {
        super(resource);
    }
}
