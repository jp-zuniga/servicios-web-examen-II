package org.uam.tecnouam.domain.exceptions;

public class PrestamoYaDevuelto extends RuntimeException {
    public PrestamoYaDevuelto(Long id) {
        super("El préstamo(id=%d) ya fue devuelto.".formatted(id));
    }
}
