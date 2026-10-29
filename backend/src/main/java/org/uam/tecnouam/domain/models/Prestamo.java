package org.uam.tecnouam.domain.models;

import java.time.LocalDate;

import org.uam.tecnouam.domain.exceptions.FechaDevolucionInvalida;
import org.uam.tecnouam.domain.exceptions.PrestamoYaDevuelto;

public record Prestamo(
    Long id,
    Equipo equipo,
    String nombreSolicitante,
    LocalDate fechaPrestamo,
    LocalDate fechaDevolucionPrevista,
    LocalDate fechaDevolucion,
    EstadoPrestamo estado
)
{
    public static Prestamo nuevo(
        Equipo equipo,
        String nombreSolicitante,
        LocalDate fechaPrestamo,
        LocalDate fechaDevolucionPrevista
    ) {
        Prestamo prestamo = new Prestamo(
            null,
            equipo,
            nombreSolicitante,
            fechaPrestamo,
            fechaDevolucionPrevista,
            null,
            EstadoPrestamo.ACTIVO
        );

        prestamo.validarFechas();

        return prestamo;
    }

    public boolean estaActivo() {
        return estado == EstadoPrestamo.ACTIVO;
    }

    public void validarFechas() {
        if (fechaDevolucionPrevista.isBefore(fechaPrestamo)) {
            throw new FechaDevolucionInvalida();
        }
    }

    public Prestamo devolver(Equipo equipoLiberado, LocalDate fecha) {
        if (!estaActivo()) {
            throw new PrestamoYaDevuelto(id);
        }

        return new Prestamo(
            id,
            equipoLiberado,
            nombreSolicitante,
            fechaPrestamo,
            fechaDevolucionPrevista,
            fecha,
            EstadoPrestamo.DEVUELTO
        );
    }
}
