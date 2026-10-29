package org.uam.tecnouam.infrastructure.persistence.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.uam.tecnouam.domain.models.EstadoPrestamo;

@AllArgsConstructor @Builder @Data @Entity @NoArgsConstructor @Table(name = "prestamo")
public class PrestamoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Muchos préstamos pertenecen a un equipo (FK prestamo.equipo_id -> equipo.id).
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipo_id", nullable = false)
    private EquipoEntity equipo;

    @Column(name = "nombre_solicitante", nullable = false, length = 100)
    private String nombreSolicitante;

    @Column(name = "fecha_prestamo", nullable = false)
    private LocalDate fechaPrestamo;

    @Column(name = "fecha_devolucion_prevista", nullable = false)
    private LocalDate fechaDevolucionPrevista;

    @Column(name = "fecha_devolucion")
    private LocalDate fechaDevolucion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPrestamo estado;
}
