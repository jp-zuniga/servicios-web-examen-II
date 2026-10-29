package org.uam.tecnouam.infrastructure.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PrestamoRequest(
    @NotNull(message = "El equipo es obligatorio.") @Positive(
        message = "El id del equipo debe ser positivo."
    ) Long equipoId,

    @NotBlank(message = "El nombre del solicitante es obligatorio.") @Size(
        min = 2, max = 100,
        message = "El nombre del solicitante debe tener entre 2 y 100 caracteres."
    ) String nombreSolicitante,

    @NotNull(message = "La fecha de préstamo es obligatoria.") LocalDate fechaPrestamo,

    @NotNull(
        message = "La fecha prevista de devolución es obligatoria."
    ) LocalDate fechaDevolucionPrevista
)
{
}
