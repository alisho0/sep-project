package dev.ale.sep_project.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import dev.ale.sep_project.models.Grado;
import dev.ale.sep_project.repository.GradoRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final GradoRepository gradoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Solo cargamos datos si no hay grados en la BD
        if (gradoRepository.count() == 0) {
            cargarGrados();
        }
    }

    private void cargarGrados() {
        String[] turnos = {"M", "T"};
        String[] secciones = {"A", "B", "C"};

        // Crear grados del 1 al 7
        for (int nroGrado = 1; nroGrado <= 7; nroGrado++) {
            for (String turno : turnos) {
                for (String seccion : secciones) {
                    Grado grado = new Grado();
                    grado.setNroGrado(nroGrado);
                    grado.setTurno(turno);
                    grado.setSeccion(seccion);
                    gradoRepository.save(grado);
                }
            }
        }
    }
}
