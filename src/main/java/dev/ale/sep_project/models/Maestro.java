package dev.ale.sep_project.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Maestro extends Persona {

    @OneToOne
    private Usuario usuario;
}
