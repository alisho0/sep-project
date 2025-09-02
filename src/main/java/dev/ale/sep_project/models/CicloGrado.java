package dev.ale.sep_project.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class CicloGrado {
    private Long id;
    private Grado grado;
    private int año;
    @OneToMany(mappedBy = "cicloGrado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroAlumno> registros;
}
