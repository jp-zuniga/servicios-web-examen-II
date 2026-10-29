package org.uam.tecnouam.infrastructure.controllers;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.uam.tecnouam.application.services.PrestamoService;
import org.uam.tecnouam.domain.models.Prestamo;
import org.uam.tecnouam.infrastructure.dto.PrestamoRequest;
import org.uam.tecnouam.infrastructure.dto.PrestamoResponse;
import org.uam.tecnouam.infrastructure.persistence.mappers.PrestamoMapper;

@RestController @RequestMapping("/api/prestamos") public class PrestamoController {
    private final PrestamoMapper mapper;
    private final PrestamoService service;

    public PrestamoController(PrestamoMapper mapper, PrestamoService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @GetMapping public ResponseEntity<List<PrestamoResponse>> getAll() {
        return ResponseEntity.ok(service.getAllPrestamos()
            .stream()
            .map(mapper::toResponse)
            .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.getPrestamo(id)));
    }

    @PostMapping public ResponseEntity<PrestamoResponse> post(
        @Valid @RequestBody PrestamoRequest request
    ) {
        Prestamo creado = service.registrar(
            request.equipoId(),
            request.nombreSolicitante().trim(),
            request.fechaPrestamo(),
            request.fechaDevolucionPrevista()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(creado));
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<PrestamoResponse> devolver(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.devolver(id)));
    }
}
