package com.universidad.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "docente") // Nombre de la tabla en la base de datos  
public class Docente extends Persona {
    @Column(name = "nro_empleado", nullable = false, unique = true) // Columna no nula y con valor único    
    private String nroEmpleado;

    @Column(name = "departamento", nullable = false) // Columna no nula
    private String departamento;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "docente_materia",
            joinColumns = @JoinColumn(name = "id_docente"),
            inverseJoinColumns = @JoinColumn(name = "id_materia")
    )
    private Set<Materia> materias = new HashSet<>();

    /**
     * Lista de evaluaciones asociadas al docente.
     */
    @OneToMany(mappedBy = "docente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EvaluacionDocente> evaluaciones; // Lista de evaluaciones asociadas al docente
}
