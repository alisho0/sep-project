package dev.ale.sep_project.config;

import dev.ale.sep_project.models.GradoSeccionTurno;
import dev.ale.sep_project.models.Seccion;
import dev.ale.sep_project.models.Turno;
import dev.ale.sep_project.repository.GradoSeccionTurnoRepository;
import dev.ale.sep_project.repository.SeccionRepository;
import dev.ale.sep_project.repository.TurnoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import dev.ale.sep_project.models.Grado;
import dev.ale.sep_project.repository.GradoRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final GradoRepository gradoRepository;
    private final GradoSeccionTurnoRepository gradoSeccionTurnoRepository;
    private final TurnoRepository turnoRepository;
    private final SeccionRepository seccionRepository;

    @Override
    public void run(String... args) throws Exception {
        // Solo cargamos datos si no hay grados en la BD
        if (gradoSeccionTurnoRepository.count() == 0) {
            cargarGrados();
        }
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
