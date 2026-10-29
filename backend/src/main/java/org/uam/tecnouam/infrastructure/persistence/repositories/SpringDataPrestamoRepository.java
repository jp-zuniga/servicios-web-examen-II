package org.uam.tecnouam.infrastructure.persistence.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.uam.tecnouam.domain.models.EstadoPrestamo;
import org.uam.tecnouam.infrastructure.persistence.entities.PrestamoEntity;

public interface SpringDataPrestamoRepository
    extends JpaRepository<PrestamoEntity, Long>
{
    @EntityGraph(attributePaths = "equipo") @Override
    List<PrestamoEntity> findAll(Sort sort);

    @EntityGraph(attributePaths = "equipo") @Override
    Optional<PrestamoEntity> findById(Long id);

    boolean existsByEquipoIdAndEstado(Long equipoId, EstadoPrestamo estado);

    @Modifying
    @Query("delete from PrestamoEntity p where p.equipo.id = :equipoId")
    void deleteByEquipoId(Long equipoId);
}
