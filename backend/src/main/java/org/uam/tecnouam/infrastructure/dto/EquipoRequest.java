package org.uam.tecnouam.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.uam.tecnouam.domain.models.TipoEquipo;

public record EquipoRequest(
    @NotBlank(message = "El código patrimonial es obligatorio.") @Size(
        max = 50, message = "El código patrimonial no puede superar 50 caracteres."
    ) String codigoPatrimonial,

    @NotBlank(message = "El nombre es obligatorio.") @Size(
        min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres."
    ) String nombre,

    @NotNull(
        message = "El tipo es obligatorio (PORTATIL, PROYECTOR o TABLETA)."
    ) TipoEquipo tipo
)
{
}
