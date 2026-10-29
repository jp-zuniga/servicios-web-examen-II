package org.uam.tecnouam.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import org.uam.tecnouam.application.repositories.PrestamoRepository;
import org.uam.tecnouam.domain.models.EstadoPrestamo;
import org.uam.tecnouam.domain.models.Prestamo;
import org.uam.tecnouam.infrastructure.persistence.mappers.PrestamoMapper;
import org.uam.tecnouam.infrastructure.persistence.repositories.SpringDataPrestamoRepository;

@Component public class PrestamoPersistenceAdapter implements PrestamoRepository {
    private final SpringDataPrestamoRepository repository;
    private final PrestamoMapper mapper;

    public PrestamoPersistenceAdapter(
        SpringDataPrestamoRepository repository,
        PrestamoMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override public Prestamo create(Prestamo prestamo) {
        return mapper.toDomain(repository.save(mapper.toEntity(prestamo)));
    }

    @Override public Prestamo update(Prestamo prestamo) {
        return mapper.toDomain(repository.save(mapper.toEntity(prestamo)));
    }

    @Override public Optional<Prestamo> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override public List<Prestamo> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"))
            .stream()
            .map(mapper::toDomain)
            .toList();
    }

    @Override public boolean existsActivoByEquipoId(Long equipoId) {
        return repository.existsByEquipoIdAndEstado(equipoId, EstadoPrestamo.ACTIVO);
    }

    @Override public void deleteByEquipoId(Long equipoId) {
        repository.deleteByEquipoId(equipoId);
    }
}
