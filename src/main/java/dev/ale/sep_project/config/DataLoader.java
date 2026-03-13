package dev.ale.sep_project.config;

import dev.ale.sep_project.models.*;
import dev.ale.sep_project.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final GradoRepository gradoRepository;
    private final GradoSeccionTurnoRepository gradoSeccionTurnoRepository;
    private final CicloGradoRepository cicloGradoRepository;
    private final TurnoRepository turnoRepository;
    private final SeccionRepository seccionRepository;
    private final UsuarioRepository usuarioRepository;
    private final MaestroRepository maestroRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Solo cargamos datos si no hay grados en la BD
        if (gradoSeccionTurnoRepository.count() == 0) {
            cargarGrados();
        }
        if (usuarioRepository.count() == 0) {
            System.out.println("Usuarios en BD: " + usuarioRepository.count());
            crearUsuarios();
        }
    }

    // aux
    private void activarGradosCicloNulos() {
        List<CicloGrado> cicloGrados = (List<CicloGrado>) cicloGradoRepository.findAll();

        cicloGrados.forEach(c -> {
            c.setEstado(EstadoCiclo.ACTIVO);
            cicloGradoRepository.save(c);
        });
    }

    private void crearUsuarios() {

        Maestro maestro = new Maestro();
        maestro.setDni("55111222");
        maestro.setNombre("John");
        maestro.setApellido("Doe");
        maestro.setDomicilio("Mza 14, L34");

        Usuario admin = Usuario.builder()
                .username("admin")
                .password(passwordEncoder.encode("123"))
                .rol(Rol.ADMIN)
                .maestro(maestro)
                .build();
        maestro.setUsuario(admin);
        maestroRepository.save(maestro);
        usuarioRepository.save(admin);

    }

    private void cargarGrados() {
        String[] turnos = {"M", "T"};
        String[] secs = {"A", "B", "C"};

        // Crear grados del 1 al 7
        for (int nroGrado = 1; nroGrado <= 7; nroGrado++) {
            int finalNroGrado = nroGrado;
            for (String turno : turnos) {
                for (String sec1 : secs) {
                    Grado grado = gradoRepository.findByNroGrado(nroGrado)
                            .orElseGet(() -> gradoRepository.save(Grado.builder().nroGrado(finalNroGrado).build()));

                    Seccion seccion = seccionRepository.findByLetra(sec1)
                            .orElseGet(() -> seccionRepository.save(Seccion.builder().letra(sec1).build()));

                    Turno tur = turnoRepository.findByNombreTurno(turno)
                            .orElseGet(() -> turnoRepository.save(Turno.builder().nombreTurno(turno).build()));

                    GradoSeccionTurno gst = GradoSeccionTurno.builder()
                            .grado(grado)
                            .seccion(seccion)
                            .turno(tur)
                            .build();
                    gradoSeccionTurnoRepository.save(gst);
                }
            }
        }
    }
}
