package dev.ale.sep_project.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Alumno extends Persona {
    private boolean discapacidad;
    private String detalleDiscap;
    @ManyToMany
    @JoinTable(
        name = "alumno_tutor",
        joinColumns = @JoinColumn(name= "alumno_id"),
        inverseJoinColumns = @JoinColumn(name="tutor_id"))
    private List<Tutor> tutores = new ArrayList<>();
    private Estados estado;

    @OneToMany(mappedBy = "alumno")
    private List<RegistroAlumno> registroAlumno;
}
