package dev.ale.sep_project.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Usuario {
    private Long id;
    private String username;
    private String password;
    private Rol rol;

    @OneToOne(mappedBy = "usuario")
    private Maestro maestro;
}
