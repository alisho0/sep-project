package dev.ale.sep_project.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

@Entity
public class Maestro extends Persona {

    @OneToOne(mappedBy = "maestro")
    private Usuario usuario;
}
