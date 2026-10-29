package org.uam.tecnouam.application.exceptions;

public class EquipoConPrestamosActivos extends RuntimeException {
    public EquipoConPrestamosActivos(Long id) {
        super("El equipo(id=%d) tiene préstamos activos y no puede eliminarse.".formatted(
            id));
    }
}
