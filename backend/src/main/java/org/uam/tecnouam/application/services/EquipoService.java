package org.uam.tecnouam.application.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.uam.tecnouam.application.exceptions.CodigoPatrimonialDuplicado;
import org.uam.tecnouam.application.exceptions.EquipoConPrestamosActivos;
import org.uam.tecnouam.application.exceptions.RecursoNoEncontrado;
import org.uam.tecnouam.application.repositories.EquipoRepository;
import org.uam.tecnouam.application.repositories.PrestamoRepository;
import org.uam.tecnouam.domain.models.Equipo;
import org.uam.tecnouam.domain.models.EstadoEquipo;

@Service public class EquipoService {
    private final EquipoRepository equipos;
    private final PrestamoRepository prestamos;

    public EquipoService(EquipoRepository equipos, PrestamoRepository prestamos) {
        this.equipos = equipos;
        this.prestamos = prestamos;
    }

    @Transactional public Equipo post(Equipo equipo) {
        if (equipos.existsByCodigoPatrimonial(equipo.codigoPatrimonial())) {
            throw new CodigoPatrimonialDuplicado(equipo.codigoPatrimonial());
        }

        return equipos.create(new Equipo(
            null,
            equipo.codigoPatrimonial(),
            equipo.nombre(),
            equipo.tipo(),
            EstadoEquipo.DISPONIBLE
        ));
    }

    @Transactional public Equipo put(Equipo cambios) {
        Equipo existente = equipos.findByIdParaActualizar(cambios.id())
            .orElseThrow(() -> new RecursoNoEncontrado("Equipo", cambios.id()));

        if (equipos.existsByCodigoPatrimonialAndIdNot(
            cambios.codigoPatrimonial(),
            cambios.id()
        )) {
            throw new CodigoPatrimonialDuplicado(cambios.codigoPatrimonial());
        }

        // El estado solo cambia mediante préstamos y devoluciones.
        return equipos.update(new Equipo(
            existente.id(),
            cambios.codigoPatrimonial(),
            cambios.nombre(),
            cambios.tipo(),
            existente.estado()
        ));
    }

    @Transactional(readOnly = true) public Equipo getEquipo(Long id) {
        return equipos.findById(id)
            .orElseThrow(() -> new RecursoNoEncontrado("Equipo", id));
    }

    @Transactional(readOnly = true) public List<Equipo> getAllEquipos() {
        return equipos.findAll();
    }

    @Transactional public void delete(Long id) {
        if (!equipos.existsById(id)) {
            throw new RecursoNoEncontrado("Equipo", id);
        }

        if (prestamos.existsActivoByEquipoId(id)) {
            throw new EquipoConPrestamosActivos(id);
        }

        // Los préstamos ya DEVUELTOS se eliminan junto con el equipo.
        prestamos.deleteByEquipoId(id);
        equipos.deleteById(id);
    }
}
