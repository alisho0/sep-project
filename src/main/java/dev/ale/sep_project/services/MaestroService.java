package dev.ale.sep_project.services;

import dev.ale.sep_project.dtos.maestros.MaestroResponseDTO;
import dev.ale.sep_project.dtos.maestros.MaestrosAsignadosDTO;
import dev.ale.sep_project.exceptions.BusinessLogicException;
import dev.ale.sep_project.models.Maestro;
import dev.ale.sep_project.repository.MaestroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MaestroService {
    private final MaestroRepository maestroRepository;

    public List<MaestrosAsignadosDTO> listarMaestrosAsignadosPorCSG(Long idCiclo) {
        try {
            List<Maestro> maestros = maestroRepository.findByCiclos_Id(idCiclo);
            return maestros.stream()
                    .map(m -> MaestrosAsignadosDTO.builder()
                            .id(m.getId())
                            .usuario(m.getApellido() + ", " + m.getNombre())
                            .correo(m.getUsuario().getUsername())
                            .build())
                    .toList();
        } catch (DataAccessException e) {
            throw new BusinessLogicException("Error al listar los maestros asignados");
        }
    }

    public List<MaestroResponseDTO> listarMaestros() {
        try {
            List<Maestro> maestros = (List<Maestro>) maestroRepository.findAll();
            List<MaestroResponseDTO> maestrosRespuesta = maestros.stream()
                    .map(m -> new MaestroResponseDTO(m.getId(), m.getNombre() + " " + m.getApellido(), m.getUsuario().getUsername()))
                    .toList();

            return maestrosRespuesta;
        } catch (DataAccessException e) {
            throw new BusinessLogicException("No se pudo listar los maestros: " + e.getMessage());
        }
    }
}
