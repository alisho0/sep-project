package dev.ale.sep_project.models;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Observacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private LocalDate fecha;

    @Column(name = "contenido", columnDefinition = "TEXT")
    private String contenido;
    
    @ManyToOne
    @JoinColumn(name = "registroAlumno_id")
    private RegistroAlumno registroAlumno;

    @Enumerated(EnumType.STRING)
    private Motivo motivo;
}
