package org.uam.tecnouam.infrastructure.persistence.mappers;

import org.springframework.stereotype.Component;

import org.uam.tecnouam.domain.models.Equipo;
import org.uam.tecnouam.infrastructure.dto.EquipoRequest;
import org.uam.tecnouam.infrastructure.dto.EquipoResponse;
import org.uam.tecnouam.infrastructure.persistence.entities.EquipoEntity;

@Component public class EquipoMapper {
    public Equipo fromRequest(Long id, EquipoRequest request) {
        if (request == null) {
            return null;
        }

        return new Equipo(
            id,
            request.codigoPatrimonial().trim(),
            request.nombre().trim(),
            request.tipo(),
            null
        );
    }

    public EquipoEntity toEntity(Equipo equipo) {
        if (equipo == null) {
            return null;
        }

        return EquipoEntity.builder()
            .id(equipo.id())
            .codigoPatrimonial(equipo.codigoPatrimonial())
            .nombre(equipo.nombre())
            .tipo(equipo.tipo())
            .estado(equipo.estado())
            .build();
    }

    public EquipoResponse toResponse(Equipo equipo) {
        if (equipo == null) {
            return null;
        }

        return new EquipoResponse(
            equipo.id(),
            equipo.codigoPatrimonial(),
            equipo.nombre(),
            equipo.tipo(),
            equipo.estado()
        );
    }

    public Equipo toDomain(EquipoEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Equipo(
            entity.getId(),
            entity.getCodigoPatrimonial(),
            entity.getNombre(),
            entity.getTipo(),
            entity.getEstado()
        );
    }
}
