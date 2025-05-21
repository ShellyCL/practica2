package com.universidad.service;

import com.universidad.dto.MateriaDTO;
import com.universidad.dto.DocenteDTO;

import java.util.List;

public interface IDocenteService {
    List<DocenteDTO> obtenerTodosLosDocentes();
    DocenteDTO obtenerDocentePorId(Long id);
    DocenteDTO obtenerDocentePorNroEmpleado(String nroEmpleado);
    DocenteDTO crearDocente(DocenteDTO docente);
    DocenteDTO actualizarDocente(Long id, DocenteDTO docente);
    void eliminarDocente(Long id);
    void asignarMateria(Long idDocente, Long idMateria);
}
