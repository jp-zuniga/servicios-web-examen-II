package org.uam.tecnouam.infrastructure.controllers;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.uam.tecnouam.application.services.EquipoService;
import org.uam.tecnouam.domain.models.Equipo;
import org.uam.tecnouam.infrastructure.dto.EquipoRequest;
import org.uam.tecnouam.infrastructure.dto.EquipoResponse;
import org.uam.tecnouam.infrastructure.persistence.mappers.EquipoMapper;

@RestController @RequestMapping("/api/equipos") public class EquipoController {
    private final EquipoMapper mapper;
    private final EquipoService service;

    public EquipoController(EquipoMapper mapper, EquipoService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @GetMapping public ResponseEntity<List<EquipoResponse>> getAll() {
        return ResponseEntity.ok(service.getAllEquipos()
            .stream()
            .map(mapper::toResponse)
            .toList());
    }

    @GetMapping("/{id}") public ResponseEntity<EquipoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.getEquipo(id)));
    }

    @PostMapping public ResponseEntity<EquipoResponse> post(
        @Valid @RequestBody EquipoRequest request
    ) {
        Equipo creado = service.post(mapper.fromRequest(null, request));

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(creado));
    }

    @PutMapping("/{id}") public ResponseEntity<EquipoResponse> put(
        @PathVariable Long id,
        @Valid @RequestBody EquipoRequest request
    ) {
        Equipo actualizado = service.put(mapper.fromRequest(id, request));

        return ResponseEntity.ok(mapper.toResponse(actualizado));
    }

    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
