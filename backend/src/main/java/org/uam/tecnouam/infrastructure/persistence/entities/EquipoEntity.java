package org.uam.tecnouam.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.uam.tecnouam.domain.models.EstadoEquipo;
import org.uam.tecnouam.domain.models.TipoEquipo;

@AllArgsConstructor @Builder @Data @Entity @NoArgsConstructor @Table(name = "equipo")
public class EquipoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_patrimonial", nullable = false, unique = true, length = 50)
    private String codigoPatrimonial;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoEquipo tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoEquipo estado;
}
