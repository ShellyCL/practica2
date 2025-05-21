package com.universidad.service.impl;

import com.universidad.dto.InscripcionDTO;
import com.universidad.model.Estudiante;
import com.universidad.model.Inscripcion;
import com.universidad.model.Materia;
import com.universidad.repository.EstudianteRepository;
import com.universidad.repository.InscripcionRepository;
import com.universidad.repository.MateriaRepository;
import com.universidad.service.IInscripcionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InscripcionServiceImpl implements IInscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final EstudianteRepository estudianteRepository;
    private final MateriaRepository materiaRepository;

    @Override
    @Transactional
    @CachePut(value = "inscripcion", key = "#result.id")
    @CacheEvict(value = "inscripciones", allEntries = true)
    public InscripcionDTO crear(InscripcionDTO dto) {
        if (inscripcionRepository.existsByEstudianteIdAndMateriaId(dto.getIdEstudiante(), dto.getIdMateria())) {
            throw new IllegalStateException("El estudiante ya está inscrito en esta materia.");
        }

        Estudiante estudiante = estudianteRepository.findById(dto.getIdEstudiante())
                .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado"));

        Materia materia = materiaRepository.findById(dto.getIdMateria())
                .orElseThrow(() -> new EntityNotFoundException("Materia no encontrada"));

        Inscripcion inscripcion = Inscripcion.builder()
                .estudiante(estudiante)
                .materia(materia)
                .fechaInscripcion(dto.getFechaInscripcion())
                .build();

        return toDTO(inscripcionRepository.save(inscripcion));
    }

    @Override
    public InscripcionDTO obtenerPorId(Long id) {
        return inscripcionRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Inscripción no encontrada"));
    }

    @Override
    @Cacheable(value = "inscripciones")
    public List<InscripcionDTO> listar() {
        return inscripcionRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CachePut(value = "inscripcion", key = "#result.id")
    @CacheEvict(value = "inscripciones", allEntries = true)
    public InscripcionDTO actualizar(Long id, InscripcionDTO dto) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inscripción no encontrada"));

        Estudiante estudiante = estudianteRepository.findById(dto.getIdEstudiante())
                .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado"));

        Materia materia = materiaRepository.findById(dto.getIdMateria())
                .orElseThrow(() -> new EntityNotFoundException("Materia no encontrada"));

        inscripcion.setEstudiante(estudiante);
        inscripcion.setMateria(materia);
        inscripcion.setFechaInscripcion(dto.getFechaInscripcion());

        return toDTO(inscripcionRepository.save(inscripcion));
    }

    @Override
    @Transactional
    @CacheEvict(value = {"inscripcion", "inscripciones"}, allEntries = true)
    public void eliminar(Long id) {
        if (!inscripcionRepository.existsById(id)) {
            throw new EntityNotFoundException("Inscripción no encontrada");
        }
        inscripcionRepository.deleteById(id);
    }


    private InscripcionDTO toDTO(Inscripcion inscripcion) {
        return InscripcionDTO.builder()
                .id(inscripcion.getId())
                .idEstudiante(inscripcion.getEstudiante().getId())
                .idMateria(inscripcion.getMateria().getId())
                .fechaInscripcion(inscripcion.getFechaInscripcion())
                .build();
    }
}
