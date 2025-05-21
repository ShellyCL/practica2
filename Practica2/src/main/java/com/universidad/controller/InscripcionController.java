package com.universidad.controller;

import com.universidad.dto.InscripcionDTO;
import com.universidad.service.IInscripcionService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscripciones")
@RequiredArgsConstructor
public class InscripcionController {

    private final IInscripcionService inscripcionService;

    @PostMapping
    @Transactional
    public ResponseEntity<InscripcionDTO> crear(@Valid @RequestBody InscripcionDTO dto) {
        return ResponseEntity.ok(inscripcionService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<InscripcionDTO>> listar() {
        return ResponseEntity.ok(inscripcionService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InscripcionDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(inscripcionService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<InscripcionDTO> actualizar(@PathVariable Long id, @Valid @RequestBody InscripcionDTO dto) {
        return ResponseEntity.ok(inscripcionService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        inscripcionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
