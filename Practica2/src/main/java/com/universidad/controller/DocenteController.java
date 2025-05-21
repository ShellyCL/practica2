package com.universidad.controller;

import com.universidad.dto.DocenteDTO;
import com.universidad.service.IDocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/docentes")
public class DocenteController {
    private final IDocenteService docenteService;
    private static final Logger logger = LoggerFactory.getLogger(DocenteController.class);

    @Autowired
    public DocenteController(IDocenteService docenteService) {
        this.docenteService = docenteService;
    }
    @GetMapping
    public ResponseEntity<List<DocenteDTO>>  obtenerTodosLosDocentes() {
        long inicio = System.currentTimeMillis();
        logger.info("[DOCENTE] Inicio obtenerTodosLosDocentes: {}", inicio);
        List<DocenteDTO> docentes = docenteService.obtenerTodosLosDocentes();
        long fin = System.currentTimeMillis();
        logger.info("[DOCENTE] Fin obtenerTodosLosDocentes: {} (Duracion: {} ms)", fin, (fin-inicio));
        return ResponseEntity.ok(docentes);
    }
    @GetMapping("/empleado/{numeroEmpleado}")
    public ResponseEntity<DocenteDTO> obtenerDocentePorNumeroInscripcion(
            @PathVariable String numeroEmpleado) {
        long inicio = System.currentTimeMillis();
        logger.info("[DOCENTE] Inicio obtenerDocentePorNumeroInscripcion: {}", inicio);
        DocenteDTO docente = docenteService.obtenerDocentePorNroEmpleado(numeroEmpleado);
        long fin = System.currentTimeMillis();
        logger.info("[DOCENTE] Fin obtenerDocentePorNumeroInscripcion: {} (Duracion: {} ms)", fin, (fin-inicio));
        return ResponseEntity.ok(docente);
    }
    @GetMapping("/{id}")
    public ResponseEntity<DocenteDTO> obtenerDocentePorId(
            @PathVariable Long id) {
        long inicio = System.currentTimeMillis();
        logger.info("[DOCENTE] Inicio obtenerDocentePorId: {}", inicio);
        DocenteDTO docente = docenteService.obtenerDocentePorId(id);
        long fin = System.currentTimeMillis();
        logger.info("[DOCENTE] Fin obtenerDocentePorId: {} (Duracion: {} ms)", fin, (fin-inicio));
        return ResponseEntity.ok(docente);
    }
    @PutMapping("/{idDocente}/asignarMateria/{idMateria}")
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> asignarMateriaADocente(@PathVariable Long idDocente, @PathVariable Long idMateria) {
        docenteService.asignarMateria(idDocente, idMateria);
        return ResponseEntity.ok("Materia asignada al docente correctamente.");
    }
}
