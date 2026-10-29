package org.uam.tecnouam.infrastructure.persistence.repositories;

import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import org.uam.tecnouam.infrastructure.persistence.entities.EquipoEntity;

public interface SpringDataEquipoRepository extends JpaRepository<EquipoEntity, Long> {
    boolean existsByCodigoPatrimonial(String codigoPatrimonial);

    boolean existsByCodigoPatrimonialAndIdNot(String codigoPatrimonial, Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from EquipoEntity e where e.id = :id")
    Optional<EquipoEntity> findByIdForUpdate(Long id);
}
