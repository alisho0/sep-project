package dev.ale.sep_project.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Maestro extends Persona {

    @OneToOne
    private Usuario usuario;

    @ManyToMany(mappedBy = "maestros")
    private List<CicloGrado> ciclos;
}
