package org.uam.tecnouam.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import org.uam.tecnouam.application.repositories.EquipoRepository;
import org.uam.tecnouam.domain.models.Equipo;
import org.uam.tecnouam.infrastructure.persistence.mappers.EquipoMapper;
import org.uam.tecnouam.infrastructure.persistence.repositories.SpringDataEquipoRepository;

@Component public class EquipoPersistenceAdapter implements EquipoRepository {
    private final SpringDataEquipoRepository repository;
    private final EquipoMapper mapper;

    public EquipoPersistenceAdapter(
        SpringDataEquipoRepository repository,
        EquipoMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override public Equipo create(Equipo equipo) {
        return mapper.toDomain(repository.save(mapper.toEntity(equipo)));
    }

    @Override public Equipo update(Equipo equipo) {
        return mapper.toDomain(repository.save(mapper.toEntity(equipo)));
    }

    @Override public Optional<Equipo> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override public Optional<Equipo> findByIdParaActualizar(Long id) {
        return repository.findByIdForUpdate(id).map(mapper::toDomain);
    }

    @Override public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override public boolean existsByCodigoPatrimonial(String codigoPatrimonial) {
        return repository.existsByCodigoPatrimonial(codigoPatrimonial);
    }

    @Override public boolean existsByCodigoPatrimonialAndIdNot(
        String codigoPatrimonial,
        Long id
    ) {
        return repository.existsByCodigoPatrimonialAndIdNot(codigoPatrimonial, id);
    }

    @Override public List<Equipo> findAll() {
        return repository.findAll(Sort.by("id")).stream().map(mapper::toDomain).toList();
    }

    @Override public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
