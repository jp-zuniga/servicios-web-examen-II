package org.uam.tecnouam.application.exceptions;

public class RecursoNoEncontrado extends RuntimeException {
    public RecursoNoEncontrado(String recurso, Long id) {
        super("%s(id=%d) no existe.".formatted(recurso, id));
    }
}
