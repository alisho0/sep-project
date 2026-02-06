package dev.ale.sep_project.models;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CicloGrado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "grado_seccion_turno_id")
    private GradoSeccionTurno gradoSeccionTurno;

    private int anio;
    
    @OneToMany(mappedBy = "cicloGrado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroAlumno> registros;

    @ManyToMany
    @JoinTable(
            name = "ciclo_grado_maestro",
            joinColumns = @JoinColumn(name = "ciclo_grado_id"),
            inverseJoinColumns = @JoinColumn(name = "maestro_id")
    )
    private List<Maestro> maestros;

    @Enumerated(EnumType.STRING)
    private EstadoCiclo estado = EstadoCiclo.ACTIVO;
}