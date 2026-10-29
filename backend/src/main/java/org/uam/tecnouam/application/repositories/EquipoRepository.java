package org.uam.tecnouam.application.repositories;

import java.util.List;
import java.util.Optional;

import org.uam.tecnouam.domain.models.Equipo;

public interface EquipoRepository {
    Equipo create(Equipo equipo);

    Equipo update(Equipo equipo);

    Optional<Equipo> findById(Long id);

    Optional<Equipo> findByIdParaActualizar(Long id);

    boolean existsById(Long id);

    boolean existsByCodigoPatrimonial(String codigoPatrimonial);

    boolean existsByCodigoPatrimonialAndIdNot(String codigoPatrimonial, Long id);

    List<Equipo> findAll();

    void deleteById(Long id);
}
