package com.universidad.repository;

import com.universidad.model.Inscripcion;
import com.universidad.model.Materia;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;


@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long>  {

    boolean existsByEstudianteIdAndMateriaId(Long idEstudiante, Long idMateria);
}
