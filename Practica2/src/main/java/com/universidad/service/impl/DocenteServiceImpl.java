package com.universidad.service.impl;

import com.universidad.dto.DocenteDTO;
import com.universidad.model.Docente;
import com.universidad.model.Materia;
import com.universidad.repository.DocenteRepository;
import com.universidad.repository.MateriaRepository;
import com.universidad.service.IDocenteService;
import com.universidad.validation.DocenteValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocenteServiceImpl implements IDocenteService {
    @Autowired
    private DocenteRepository docenteRepository; // Inyección de dependencias del repositorio de docentes

    @Autowired
    private MateriaRepository materiaRepository;
    @Autowired // Inyección de dependencias del validador de docentes
    private DocenteValidator docenteValidator; // Declara una variable para el validador de docentes

    public DocenteServiceImpl(DocenteRepository docenteRepository, DocenteValidator docenteValidator) {
        this.docenteRepository = docenteRepository;
        this.docenteValidator = docenteValidator;
    }

    @Override
    @Cacheable(value = "docentes")
    public List<DocenteDTO> obtenerTodosLosDocentes() {
        // Obtiene todos los docentes y los convierte a DTO
        return docenteRepository.findAll().stream() // Obtiene todos los docentes de la base de datos
                .map(this::convertToDTO) // Convierte cada Docente a DocenteDTO
                .collect(Collectors.toList()); // Recoge los resultados en una lista
    }

    @Override
    @Transactional
    @Cacheable(value = "docente", key = "#idDocente")
    public DocenteDTO obtenerDocentePorId(Long idDocente) {
        Docente docente = docenteRepository.findById(idDocente)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
        return convertToDTO(docente);
    }

    @Override
    @Cacheable(value = "docente", key = "#nroEmpleado")
    public DocenteDTO obtenerDocentePorNroEmpleado(String nroEmpleado) {
        // Busca un docente por su número de empleado y lo convierte a DTO
        Docente docente = docenteRepository.findByNroEmpleado(nroEmpleado); // Busca el docente por su número de empleado
        return convertToDTO(docente); // Convierte el docente a DocenteDTO y lo retorna
    }

    @Override
    @Transactional
    @CacheEvict(value = "materiasDocente", key = "#idDocente")
    public void asignarMateria(Long idDocente, Long idMateria) {
        Docente docente = docenteRepository.findById(idDocente)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        Materia materia = materiaRepository.findById(idMateria)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));

        if (!docente.getMaterias().contains(materia)) {
            docente.getMaterias().add(materia);
            docenteRepository.save(docente);
        }
    }


    @Override
    @CachePut(value = "docente", key = "#result.nroEmpleado")
    public DocenteDTO crearDocente(DocenteDTO docenteDTO) { // Método para crear un nuevo docente

        docenteValidator.validacionCompletaDocente(docenteDTO); // Valida el docente usando el validador

        // Convierte el DTO a entidad, guarda el docente y lo convierte de nuevo a DTO
        Docente docente = convertToEntity(docenteDTO); // Convierte el DocenteDTO a Docente
        Docente docenteGuardado = docenteRepository.save(docente); // Guarda el Docente en la base de datos
        return convertToDTO(docenteGuardado); // Convierte el docente guardado a DocenteDTO y lo retorna
    }

    @Override
    @CachePut(value = "docente", key = "#id")
    @CacheEvict(value = {"docentes"}, allEntries = true)
    public DocenteDTO actualizarDocente(Long id, DocenteDTO docenteDTO) { // Método para actualizar un docente existente
        // Busca el docente por su ID, actualiza sus datos y lo guarda de nuevo
        Docente docenteExistente = docenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado")); // Lanza una excepción si el docente no se encuentra
        docenteExistente.setNombre(docenteDTO.getNombre()); // Actualiza el nombre
        docenteExistente.setApellido(docenteDTO.getApellido()); // Actualiza el apellido
        docenteExistente.setEmail(docenteDTO.getEmail()); // Actualiza el email
        docenteExistente.setFechaNacimiento(docenteDTO.getFechaNacimiento()); // Actualiza la fecha de nacimiento
        docenteExistente.setNroEmpleado(docenteDTO.getNroEmpleado()); // Actualiza el número de empleado
        docenteExistente.setDepartamento(docenteDTO.getDepartamento());
        Docente docenteActualizado = docenteRepository.save(docenteExistente); // Guarda el docente actualizado en la base de datos
        return convertToDTO(docenteActualizado); // Convierte el docente actualizado a DocenteDTO y lo retorna
    }

    @Override
    @CacheEvict(value = {"docente", "docentes"}, allEntries = true)
    public void eliminarDocente(Long id) {
        docenteRepository.deleteById(id);
    }


    // Método auxiliar para convertir entidad a DTO
    private DocenteDTO convertToDTO(Docente docente) { // Método para convertir un Estudiante a EstudianteDTO
        return DocenteDTO.builder() // Usa el patrón builder para crear un EstudianteDTO
                .id(docente.getId()) // Asigna el ID
                .nombre(docente.getNombre()) // Asigna el nombre
                .apellido(docente.getApellido()) // Asigna el apellido
                .email(docente.getEmail()) // Asigna el email
                .fechaNacimiento(docente.getFechaNacimiento()) // Asigna la fecha de nacimiento
                .nroEmpleado(docente.getNroEmpleado()) // Asigna el número de inscripción
                .departamento(docente.getDepartamento())
                .build(); // Construye el objeto EstudianteDTO
    }

    // Método auxiliar para convertir DTO a entidad
    private Docente convertToEntity(DocenteDTO docenteDTO) { // Método para convertir un EstudianteDTO a Estudiante
        return Docente.builder() // Usa el patrón builder para crear un Estudiante
                .id(docenteDTO.getId()) // Asigna el ID
                .nombre(docenteDTO.getNombre()) // Asigna el nombre
                .apellido(docenteDTO.getApellido()) // Asigna el apellido
                .email(docenteDTO.getEmail()) // Asigna el email
                .fechaNacimiento(docenteDTO.getFechaNacimiento()) // Asigna la fecha de nacimiento
                .nroEmpleado(docenteDTO.getNroEmpleado())  // Asigna el número de inscripción
                .departamento(docenteDTO.getDepartamento()) // Asigna el usuario de alta
                .build(); // Construye el objeto Estudiante
    }
}
