package dev.ale.sep_project.models;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Observacion {
    private Long id;
    private CicloGrado cicloGrado;
    private Usuario usuario;
    private LocalDate fecha;
    private String contenido;
    @ManyToOne
    @JoinColumn(name = "registroAlumno_id")
    private RegistroAlumno registroAlumno;
}
