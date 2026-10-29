package org.uam.tecnouam.infrastructure.dto;

import java.time.LocalDate;

import org.uam.tecnouam.domain.models.EstadoPrestamo;

public record PrestamoResponse(
    Long id,
    EquipoResponse equipo,
    String nombreSolicitante,
    LocalDate fechaPrestamo,
    LocalDate fechaDevolucionPrevista,
    LocalDate fechaDevolucion,
    EstadoPrestamo estado
)
{
}
