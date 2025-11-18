package dev.ale.sep_project.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
}
