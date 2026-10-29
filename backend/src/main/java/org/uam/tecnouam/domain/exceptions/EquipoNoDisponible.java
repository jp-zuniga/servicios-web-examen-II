package org.uam.tecnouam.domain.exceptions;

public class EquipoNoDisponible extends RuntimeException {
    public EquipoNoDisponible(String codigoPatrimonial) {
        super("El equipo %s ya está PRESTADO.".formatted(codigoPatrimonial));
    }
}
