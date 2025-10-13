package dev.ale.sep_project.dtos.registros;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistroAniosDTO {
    private Long id;
    private Integer anio;
}
