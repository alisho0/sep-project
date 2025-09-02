package dev.ale.sep_project.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Grado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int nroGrado;
    private String seccion;
    private String turno;
    @OneToMany(mappedBy = "grado")
    private List<CicloGrado> cicloGrado;
}
