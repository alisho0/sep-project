package dev.ale.sep_project.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dev.ale.sep_project.dtos.grados.CiclosInfoDTO;
import dev.ale.sep_project.models.CicloGrado;
import dev.ale.sep_project.models.GradoSeccionTurno;
import dev.ale.sep_project.models.Seccion;
import dev.ale.sep_project.repository.CicloGradoRepository;
import dev.ale.sep_project.repository.GradoSeccionTurnoRepository;
import dev.ale.sep_project.repository.SeccionRepository;
import org.springframework.stereotype.Service;

import dev.ale.sep_project.dtos.grados.GradoDetalleDTO;
import dev.ale.sep_project.dtos.grados.GradoListaDTO;
import dev.ale.sep_project.exceptions.ResourceNotFoundException;
import dev.ale.sep_project.models.Grado;
import dev.ale.sep_project.repository.GradoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GradoService {
    private final GradoRepository gradoRepository;
    private final SeccionRepository seccionRepository;
    private final GradoSeccionTurnoRepository gradoSeccionTurnoRepository;
    private final CicloGradoRepository cicloGradoRepository;
    private final CicloGradoService cicloGradoService;  // Inyectamos el servicio de ciclos

    public List<GradoListaDTO> listarGrados() {
        List<Integer> grados = getGrados(); // ej. [1, 2, 3, ..., 47]
        List<GradoListaDTO> resultado = new ArrayList<>();

        for (Integer nroGrado : grados) {
            List<CicloGrado> ciclos = cicloGradoRepository.findByGradoSeccionTurno_Grado_NroGrado(nroGrado);

            List<CiclosInfoDTO> ciclosDTO = ciclos.stream()
                    .map(ciclo -> new CiclosInfoDTO(
                            ciclo.getId(),
                            ciclo.getAnio(),
                            ciclo.getRegistros().size()
                    ))
                    .collect(Collectors.toList());

            GradoListaDTO dto = GradoListaDTO.builder()
                    .grado(nroGrado)
                    .ciclos(ciclosDTO)
                    .build();
            // Si tenés un id de grado, lo podés setear también:
            // dto.setId(gradoRepository.getIdPorNroGrado(nroGrado));

            resultado.add(dto);
        }
        return resultado;
    }


    public GradoDetalleDTO detalleGrado(Long id) {
        Grado grado = gradoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Grado", id));
        
        GradoListaDTO gradoDto = GradoListaDTO.builder()
            .id(grado.getId())
            .grado(grado.getNroGrado())
            .build();
        
        return GradoDetalleDTO.builder()
            .gradoListaDTO(gradoDto)
            .gradoCiclos(cicloGradoService.listarCiclosGrado(id))  // Usamos el servicio de ciclos
            .build();
    }

    // Métodos específicos de grado
    public Grado obtenerGrado(Long id) {
        return gradoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Grado", id));
    }

    public List<Integer> getGrados() {
        return gradoRepository.findByNroGradoDesc();
    }

    public List<String> getSecciones() {
        return seccionRepository.findSeccionesDisponibles();
    }

    public boolean existeGrado(int nroGrado, String seccion, String turno) {
        return gradoSeccionTurnoRepository.existsByGrado_NroGradoAndSeccion_LetraAndTurno_NombreTurno(nroGrado, seccion, turno);
    }

    public GradoSeccionTurno getGradoByNroSeccionTurno(int nroGrado, String seccion, String turno) {
        GradoSeccionTurno grado = gradoSeccionTurnoRepository.findByGrado_NroGradoAndSeccion_LetraAndTurno_NombreTurno(nroGrado, seccion, turno)
            .orElseThrow(() -> new ResourceNotFoundException("No se encontró el grado"));
        return grado;
    }
    // Aquí podrías agregar más métodos específicos de grado
    // como crear grado, actualizar grado, etc.
}

