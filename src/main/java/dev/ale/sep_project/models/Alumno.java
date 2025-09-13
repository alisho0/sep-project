package dev.ale.sep_project.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
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
    private Boolean discapacidad;
    private String detalleDiscap;
    @ManyToMany
    @JoinTable(
        name = "alumno_tutor",
        joinColumns = @JoinColumn(name= "alumno_id"),
        inverseJoinColumns = @JoinColumn(name="tutor_id"))
    private List<Tutor> tutores = new ArrayList<>();
    private Estados estado;

    @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroAlumno> registroAlumno = new ArrayList<>();
}
