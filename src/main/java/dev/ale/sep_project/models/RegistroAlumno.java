package dev.ale.sep_project.models;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RegistroAlumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "alumno_id")
    private Alumno alumno;
    
    @OneToMany(mappedBy = "registroAlumno", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Observacion> observaciones;
    
    @ManyToOne
    @JoinColumn(name = "ciclo_grado_id")
    private CicloGrado cicloGrado;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio = LocalDate.now();

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;
}
