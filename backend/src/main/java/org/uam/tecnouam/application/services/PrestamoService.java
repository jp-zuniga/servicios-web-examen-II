package org.uam.tecnouam.application.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.uam.tecnouam.application.exceptions.RecursoNoEncontrado;
import org.uam.tecnouam.application.repositories.EquipoRepository;
import org.uam.tecnouam.application.repositories.PrestamoRepository;
import org.uam.tecnouam.domain.models.Equipo;
import org.uam.tecnouam.domain.models.Prestamo;

@Service public class PrestamoService {
    private final PrestamoRepository prestamos;
    private final EquipoRepository equipos;

    public PrestamoService(PrestamoRepository prestamos, EquipoRepository equipos) {
        this.prestamos = prestamos;
        this.equipos = equipos;
    }

    @Transactional public Prestamo registrar(
        Long equipoId,
        String nombreSolicitante,
        LocalDate fechaPrestamo,
        LocalDate fechaDevolucionPrevista
    ) {
        // Bloquea la fila del equipo para que dos préstamos simultáneos no lo tomen.
        Equipo equipo = equipos.findByIdParaActualizar(equipoId)
            .orElseThrow(() -> new RecursoNoEncontrado("Equipo", equipoId));

        Prestamo prestamo = Prestamo.nuevo(
            equipo,
            nombreSolicitante,
            fechaPrestamo,
            fechaDevolucionPrevista
        );

        Equipo prestado = equipos.update(equipo.prestar());

        return prestamos.create(new Prestamo(
            null,
            prestado,
            prestamo.nombreSolicitante(),
            prestamo.fechaPrestamo(),
            prestamo.fechaDevolucionPrevista(),
            null,
            prestamo.estado()
        ));
    }

    @Transactional public Prestamo devolver(Long id) {
        Prestamo prestamo = prestamos.findById(id)
            .orElseThrow(() -> new RecursoNoEncontrado("Prestamo", id));

        Equipo equipo = equipos.findByIdParaActualizar(prestamo.equipo().id())
            .orElseThrow(() -> new RecursoNoEncontrado(
                "Equipo",
                prestamo.equipo().id()
            ));

        Prestamo devuelto = prestamo.devolver(equipo.liberar(), LocalDate.now());

        equipos.update(devuelto.equipo());

        return prestamos.update(devuelto);
    }

    @Transactional(readOnly = true) public Prestamo getPrestamo(Long id) {
        return prestamos.findById(id)
            .orElseThrow(() -> new RecursoNoEncontrado("Prestamo", id));
    }

    @Transactional(readOnly = true)
    public List<Prestamo> getAllPrestamos() {
        return prestamos.findAll();
    }
}
