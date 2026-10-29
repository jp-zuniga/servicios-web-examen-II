package org.uam.tecnouam.domain.models;

import org.uam.tecnouam.domain.exceptions.EquipoNoDisponible;

public record Equipo(
    Long id,
    String codigoPatrimonial,
    String nombre,
    TipoEquipo tipo,
    EstadoEquipo estado
)
{
    public boolean estaDisponible() {
        return estado == EstadoEquipo.DISPONIBLE;
    }

    public Equipo prestar() {
        if (!estaDisponible()) {
            throw new EquipoNoDisponible(codigoPatrimonial);
        }

        return conEstado(EstadoEquipo.PRESTADO);
    }

    public Equipo liberar() {
        return conEstado(EstadoEquipo.DISPONIBLE);
    }

    private Equipo conEstado(EstadoEquipo nuevoEstado) {
        return new Equipo(id, codigoPatrimonial, nombre, tipo, nuevoEstado);
    }
}
