package com.universidad.repository;

import com.universidad.model.Docente;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {

    Boolean existsByEmail(String email); // Método para verificar si existe un docente por su correo electrónico
    Boolean existsByNroEmpleado(String nroEmpleado);

    // Método para encontrar un docente por su número de empleado
    Docente findByNroEmpleado(String nroEmpleado);

    // Método para encontrar un docente por su departamento
    Docente findByDepartamento(String departamento);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Docente> findById(Long id); // Método para encontrar un estudiante por su ID con bloqueo pesimista
    // Este método se utiliza para evitar condiciones de carrera al actualizar el docente
}
