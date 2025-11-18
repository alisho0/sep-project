package dev.ale.sep_project.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity(name = "grado_seccion_turno")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradoSeccionTurno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_grado")
    private Grado grado;

    @ManyToOne
    @JoinColumn(name = "id_seccion")
    private Seccion seccion;

    @ManyToOne
    @JoinColumn(name = "id_turno")
    private Turno turno;

    @OneToMany(mappedBy = "gradoSeccionTurno")
    private List<CicloGrado> cicloGrado;
}
