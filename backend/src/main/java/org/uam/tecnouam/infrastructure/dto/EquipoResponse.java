package org.uam.tecnouam.infrastructure.dto;

import org.uam.tecnouam.domain.models.EstadoEquipo;
import org.uam.tecnouam.domain.models.TipoEquipo;

public record EquipoResponse(
    Long id,
    String codigoPatrimonial,
    String nombre,
    TipoEquipo tipo,
    EstadoEquipo estado
)
{
}
