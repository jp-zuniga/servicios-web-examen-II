package org.uam.tecnouam.application.repositories;

import java.util.List;
import java.util.Optional;

import org.uam.tecnouam.domain.models.Prestamo;

public interface PrestamoRepository {
    Prestamo create(Prestamo prestamo);

    Prestamo update(Prestamo prestamo);

    Optional<Prestamo> findById(Long id);

    List<Prestamo> findAll();

    boolean existsActivoByEquipoId(Long equipoId);

    void deleteByEquipoId(Long equipoId);
}
