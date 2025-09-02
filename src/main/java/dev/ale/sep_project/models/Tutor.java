package dev.ale.sep_project.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Tutor extends Persona{
    private String telefono;
    private String telAux;

    @ManyToMany(mappedBy = "tutores")
    private List<Alumno> alumnos;
}
