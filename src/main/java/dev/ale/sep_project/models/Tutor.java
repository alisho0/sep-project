package dev.ale.sep_project.models;

import java.util.List;

import org.hibernate.annotations.OnDelete;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tutor extends Persona {
    private String telefono;
    private String telAux;

    @ManyToMany(mappedBy = "tutores")
    @OnDelete(action = org.hibernate.annotations.OnDeleteAction.NO_ACTION)
    private List<Alumno> alumnos;
}
