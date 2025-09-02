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
import lombok.Data;

@Entity
@Data
public class CicloGrado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "grado_id")
    private Grado grado;

    private int anio;
    
    @OneToMany(mappedBy = "cicloGrado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroAlumno> registros;
}
