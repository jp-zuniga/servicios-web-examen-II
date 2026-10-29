package org.uam.tecnouam.infrastructure.persistence.mappers;

import org.springframework.stereotype.Component;

import org.uam.tecnouam.domain.models.Prestamo;
import org.uam.tecnouam.infrastructure.dto.PrestamoResponse;
import org.uam.tecnouam.infrastructure.persistence.entities.PrestamoEntity;

@Component public class PrestamoMapper {
    private final EquipoMapper equipoMapper;

    public PrestamoMapper(EquipoMapper equipoMapper) {
        this.equipoMapper = equipoMapper;
    }

    public PrestamoEntity toEntity(Prestamo prestamo) {
        if (prestamo == null) {
            return null;
        }

        return PrestamoEntity.builder()
            .id(prestamo.id())
            .equipo(equipoMapper.toEntity(prestamo.equipo()))
            .nombreSolicitante(prestamo.nombreSolicitante())
            .fechaPrestamo(prestamo.fechaPrestamo())
            .fechaDevolucionPrevista(prestamo.fechaDevolucionPrevista())
            .fechaDevolucion(prestamo.fechaDevolucion())
            .estado(prestamo.estado())
            .build();
    }

    public PrestamoResponse toResponse(Prestamo prestamo) {
        if (prestamo == null) {
            return null;
        }

        return new PrestamoResponse(
            prestamo.id(),
            equipoMapper.toResponse(prestamo.equipo()),
            prestamo.nombreSolicitante(),
            prestamo.fechaPrestamo(),
            prestamo.fechaDevolucionPrevista(),
            prestamo.fechaDevolucion(),
            prestamo.estado()
        );
    }

    public Prestamo toDomain(PrestamoEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Prestamo(
            entity.getId(),
            equipoMapper.toDomain(entity.getEquipo()),
            entity.getNombreSolicitante(),
            entity.getFechaPrestamo(),
            entity.getFechaDevolucionPrevista(),
            entity.getFechaDevolucion(),
            entity.getEstado()
        );
    }
}
