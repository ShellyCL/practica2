package com.universidad.validation;

import org.springframework.stereotype.Component;
import com.universidad.dto.DocenteDTO;
import com.universidad.repository.DocenteRepository;

import java.util.Arrays;
import java.util.List;

@Component
public class DocenteValidator {
    private final DocenteRepository docenteRepository;

    public DocenteValidator(DocenteRepository userRepository) {
        this.docenteRepository = userRepository;
    }

    public void validaEmailUnico(String email) {
        if (docenteRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Ya existe un usuario con este email");
        }
    }

    public void validaDominioEmail(String email) {
        String dominio = email.substring(email.indexOf('@') + 1);
        List<String> dominiosBloqueados = Arrays.asList("dominiobloqueado.com", "spam.com");

        if (dominiosBloqueados.contains(dominio)) {
            throw new IllegalArgumentException("El dominio de email no está permitido");
        }
    }

    // Validación manual para nombre vacío o nulo
    public void validaNombreDocente(String nombre){
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío o nulo.");
        }
    }

    // Validación manual para apellido vacío o nulo
    public void validaApellidoDocente(String apellido){
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido es obligatorio y no puede estar vacío.");
        }
    }

    public void validacionCompletaDocente(DocenteDTO docente) {
        validaEmailUnico(docente.getEmail());
        validaDominioEmail(docente.getEmail());
        validaNombreDocente(docente.getNombre());
        validaApellidoDocente(docente.getApellido());
        // Otras validaciones...
    }

    public class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}
