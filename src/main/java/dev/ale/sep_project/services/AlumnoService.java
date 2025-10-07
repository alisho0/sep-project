package dev.ale.sep_project.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dev.ale.sep_project.dtos.alumnos.AlumnoCreateDTO;
import dev.ale.sep_project.dtos.alumnos.AlumnoDetalleDTO;
import dev.ale.sep_project.dtos.alumnos.AlumnoResponseDTO;
import dev.ale.sep_project.dtos.alumnos.AlumnoUpdateDTO;
import dev.ale.sep_project.dtos.tutor.TutorRespuestaDTO;
import dev.ale.sep_project.models.Alumno;
import dev.ale.sep_project.models.CicloGrado;
import dev.ale.sep_project.models.Grado;
import dev.ale.sep_project.models.RegistroAlumno;
import dev.ale.sep_project.models.Tutor;
import dev.ale.sep_project.repository.AlumnoRepository;
import dev.ale.sep_project.repository.CicloGradoRepository;
import dev.ale.sep_project.repository.GradoRepository;
import dev.ale.sep_project.repository.RegistroAlumnoRepository;
import dev.ale.sep_project.repository.TutorRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final TutorRepository tutorRepository;
    private final CicloGradoRepository cicloGradoRepository;
    private final GradoRepository gradoRepository;
    private final RegistroAlumnoRepository registroAlumnoRepository;

    public void crearAlumno(AlumnoCreateDTO alumnoDto) throws Exception {
        System.out.println(alumnoDto);
        if (alumnoRepository.findByDni(alumnoDto.getDni()).isPresent()) {
            throw new Exception("El alumno ya existe");
        }
        try {
            Alumno alumno = new Alumno();
            alumno.setNombre(alumnoDto.getNombre());
            alumno.setApellido(alumnoDto.getApellido());
            alumno.setDiscapacidad(alumnoDto.getDiscapacidad());
            alumno.setDetalleDiscap(alumnoDto.getDetalleDiscap());
            alumno.setDomicilio(alumnoDto.getDomicilio());
            alumno.setDni(alumnoDto.getDni());

            // Inicializa la lista de tutores si es null
            if (alumno.getTutores() == null) {
                alumno.setTutores(new ArrayList<>());
            }

            for (Long tutorId : alumnoDto.getTutoresIds()) {
                if (!tutorRepository.existsById(tutorId)) {
                    throw new Exception("El tutor con ID " + tutorId + " no existe");
                }
                Tutor tutor = tutorRepository.findById(tutorId)
                        .orElseThrow(() -> new Exception("El tutor con ID " + tutorId + " no existe"));
                alumno.getTutores().add(tutor);
            }

            // Crear el primer registro automáticamente con la fecha de creación
            RegistroAlumno registroAlumno = new RegistroAlumno();
            registroAlumno.setAlumno(alumno);

            // Inicializar CicloGrado y Grado

            // A futuro optaríamos por la creación de grados desde otro lado, pero por ahora
            // lo hacemos acá
            Grado grado = gradoRepository.findByNroGradoAndSeccionAndTurno(
                    alumnoDto.getNroGrado(),
                    alumnoDto.getSeccionGrado(),
                    alumnoDto.getTurnoGrado()).orElseThrow(() -> new Exception("El grado especificado no existe"));

            // Si el ciclo grado no existe, lo creamos por el año y el grado
            if (!cicloGradoRepository.findByAnio(alumnoDto.getAnioCicloGrado()).isPresent()) {
                CicloGrado cicloGrado = new CicloGrado();
                cicloGrado.setAnio(alumnoDto.getAnioCicloGrado());
                cicloGrado.setGrado(grado);
                cicloGradoRepository.save(cicloGrado);
            }

            // Obtenemos el ciclo grado existente
            CicloGrado cicloGrado = cicloGradoRepository.findByAnio(alumnoDto.getAnioCicloGrado())
                    .orElseThrow(() -> new Exception("El ciclo grado especificado no existe"));

            registroAlumno.setCicloGrado(cicloGrado);
            // El resto de campos pueden quedar null o con valores por defecto

            // Inicializa la lista de registros si es null
            if (alumno.getRegistroAlumno() == null) {
                alumno.setRegistroAlumno(new ArrayList<>());
            }

            // Después el alumno
            alumno = alumnoRepository.save(alumno);

            // Finalmente el registro que referencia tanto al alumno como al ciclo grado
            alumno.getRegistroAlumno().add(registroAlumno);
            registroAlumnoRepository.save(registroAlumno);
        } catch (Exception e) {
            throw new Exception(e.getMessage().toString());
        }
    }

    // Método que traer todos los alumnos con detalles mínimos.
    public List<AlumnoResponseDTO> obtenerAlumnos() throws Exception {
        try {
            List<Alumno> alumnos = (List<Alumno>) alumnoRepository.findAll();
            System.out.println("Alumnos encontrados: " + alumnos.size());
            List<AlumnoResponseDTO> alumnosDTO = new ArrayList<>();

            for (Alumno alu : alumnos) {
                System.out.println("El alumno es: " + alu.getNombre() + " " + alu.getApellido());
                RegistroAlumno ultimoRegistro = alu.getRegistroAlumno().stream()
                        .sorted(Comparator.comparing(RegistroAlumno::getFechaInicio).reversed())
                        .findFirst()
                        .orElseThrow(() -> new Exception("No se encontraron registros"));
                System.out.println("El último registro es: " + ultimoRegistro.getFechaInicio());
                AlumnoResponseDTO alumnoRespuesta = AlumnoResponseDTO.builder()
                        .id(alu.getId())
                        .nombre(alu.getNombre())
                        .apellido(alu.getApellido())
                        .dni(alu.getDni())
                        .ultGrado(ultimoRegistro.getCicloGrado().getGrado().getNroGrado())
                        .seccionGrado(ultimoRegistro.getCicloGrado().getGrado().getSeccion())
                        .turno(ultimoRegistro.getCicloGrado().getGrado().getTurno())
                        .build();
                System.out.println("Testeo del dto: " + alumnoRespuesta);
                alumnosDTO.add(alumnoRespuesta);
            }

            return alumnosDTO;
        } catch (Exception e) {
            throw new Exception(e.getMessage().toString());
        }
    }

    // Obtiene el detalle de UN alumno
    public AlumnoDetalleDTO obtenerAlumno(Long id) {
        Alumno alumno = alumnoRepository.findById(id).orElseThrow(() -> new RuntimeException("El alumno no existe"));
        return AlumnoDetalleDTO.builder()
                .id(alumno.getId())
                .nombre(alumno.getNombre())
                .apellido(alumno.getApellido())
                .dni(alumno.getDni())
                .domicilio(alumno.getDomicilio())
                .discapacidad(alumno.getDiscapacidad())
                .detalleDiscap(alumno.getDetalleDiscap())
                .tutores(alumno.getTutores().stream()
                        .map(tutor -> TutorRespuestaDTO.builder()
                                .id(tutor.getId())
                                .nombre(tutor.getNombre())
                                .apellido(tutor.getApellido())
                                .dni(tutor.getDni())
                                .build())
                        .collect(Collectors.toList()))
                .build();

    }

    public void eliminarAlumno(Long id) throws Exception {
        try {
            if (!alumnoRepository.existsById(id)) {
                throw new Exception("El alumno no existe");
            }
            alumnoRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception(e.getMessage().toString());
        }
    }

    // Creo que puedo utilizar el DTO de creación, tiene la misma estructura, solo
    // habría que sacarle la lista de tutores, o adaptarla para no mandar nada
    public void actualizarAlumno(Long id, AlumnoUpdateDTO alumnoDto) throws Exception {
        try {
            if (!alumnoRepository.existsById(id)) {
                throw new Exception("El alumno no existe");
            }
            Alumno alumno = alumnoRepository.findById(id).orElseThrow(() -> new Exception("El alumno no existe"));
            alumno.setNombre(alumnoDto.getNombre());
            alumno.setApellido(alumnoDto.getApellido());
            alumno.setDiscapacidad(alumnoDto.getDiscapacidad());
            alumno.setDetalleDiscap(alumnoDto.getDetalleDiscap());
            alumno.setDomicilio(alumnoDto.getDomicilio());
            alumno.setDni(alumnoDto.getDni());
            alumnoRepository.save(alumno);
        } catch (Exception e) {
            throw new Exception(e.getMessage().toString());
        }
    }
}
