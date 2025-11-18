package dev.ale.sep_project.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity(name = "seccion")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String letra;
    @OneToMany(mappedBy = "seccion")
    private List<GradoSeccionTurno> combinaciones;

}
