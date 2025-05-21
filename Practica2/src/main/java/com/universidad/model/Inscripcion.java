package com.universidad.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter // Genera un getter para todos los campos de la clase
@Setter // Genera un setter para todos los campos de la clase
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "inscripciones", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_estudiante", "id_materia"})
})

public class Inscripcion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_estudiante")
    private Estudiante estudiante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_materia")
    private Materia materia;

    @Column(name = "fecha_inscripcion", nullable = false)
    @Temporal(TemporalType.DATE)
    @Basic(optional = false)
    private LocalDate fechaInscripcion;

    @Version
    private Long version;
}
