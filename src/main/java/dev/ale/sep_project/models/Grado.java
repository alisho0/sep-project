package dev.ale.sep_project.models;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Grado {
    private Long id;
    private int nroGrado;
    private String seccion;
    private String turno;

}
