package dev.ale.sep_project.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import dev.ale.sep_project.dtos.alumnos.*;
import dev.ale.sep_project.models.*;
import dev.ale.sep_project.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.ale.sep_project.dtos.registros.TutorListaDTO;
import dev.ale.sep_project.dtos.tutor.TutorRespuestaDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final TutorRepository tutorRepository;
    private final CicloGradoRepository cicloGradoRepository;
    private final GradoRepository gradoRepository;
    private final RegistroAlumnoRepository registroAlumnoRepository;
    private final GradoService gradoService;
    private final CicloGradoService cicloGradoService;
    private final DiscapacidadRepository discapacidadRepository;

    @Transactional
    public AlumnoResponseDTO crearAlumno(AlumnoCreateDTO alumnoDto) throws Exception {
        if (alumnoRepository.findByDni(alumnoDto.getDni()).isPresent()) {
            throw new Exception("Ya existe un alumno con el DNI: " + alumnoDto.getDni());
        }
        try {

            // Crear alumno solo con datos básicos
            Alumno alumno = new Alumno();
            alumno.setNombre(alumnoDto.getNombre());
            alumno.setApellido(alumnoDto.getApellido());
            alumno.setDiscapacidad(alumnoDto.getDiscapacidad());
            alumno.setDetalleDiscap(alumnoDto.getDetalleDiscap());
            alumno.setDomicilio(alumnoDto.getDomicilio());
            alumno.setDni(alumnoDto.getDni());
            if (!alumnoDto.getDiscapacidadesSeleccionadas().isEmpty()) {
                List<Long> ids = alumnoDto.getDiscapacidadesSeleccionadas();
                List<Discapacidad> discapacidades = discapacidadRepository.findAllById(ids).stream().toList();

                if (discapacidades.size() != ids.size()) {
                    throw new Exception("Alguna discapacidad no existe");
                }

                alumno.setDiscapacidades(discapacidades);
            }


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
            
            // Finalmente el registro que referencia tanto al alumno como al ciclo grado

            // 1. Partimos por el registro
            RegistroAlumno primerRegistro = new RegistroAlumno();

            if (!gradoService.existeGrado(alumnoDto.getNroGrado(), alumnoDto.getSeccionGrado(), alumnoDto.getTurnoGrado())) {
                throw new Exception("No existe el grado enviado");
            }
            // traemos el grado
            GradoSeccionTurno grado = gradoService.getGradoByNroSeccionTurno(alumnoDto.getNroGrado(), alumnoDto.getSeccionGrado(), alumnoDto.getTurnoGrado());

            if (!cicloGradoService.existeCicloGrado(alumnoDto.getAnioCicloGrado(), grado)) {
                throw new Exception("No existe el ciclo grado enviado");
            }

            CicloGrado ciclo = cicloGradoService.getCicloGrado(alumnoDto.getAnioCicloGrado(), grado);
            primerRegistro.setCicloGrado(ciclo);
            primerRegistro.setAlumno(alumno);

            registroAlumnoRepository.save(primerRegistro);
            alumno.getRegistroAlumno().add(primerRegistro);
            
            alumnoRepository.save(alumno);
            
            RegistroAlumno ultimoRegistro = alumno.getRegistroAlumno().stream()
                .sorted(Comparator.comparing(RegistroAlumno::getFechaInicio).reversed())
                .findFirst()
                .orElseThrow(() -> new Exception("No se encontraron registros"));

            return AlumnoResponseDTO.builder()
                .id(alumno.getId())
                .nombre(alumno.getNombre())
                .apellido(alumno.getApellido())
                .dni(alumno.getDni())
                .seccionGrado(ultimoRegistro.getCicloGrado().getGradoSeccionTurno().getSeccion().getLetra() != null ? ultimoRegistro.getCicloGrado().getGradoSeccionTurno().getSeccion().getLetra() : "Sin sección")
                .turno(ultimoRegistro.getCicloGrado().getGradoSeccionTurno().getTurno().getNombreTurno() != null ? ultimoRegistro.getCicloGrado().getGradoSeccionTurno().getTurno().getNombreTurno() : "Sin turno")
                .ultGrado(ultimoRegistro.getCicloGrado().getGradoSeccionTurno().getGrado().getNroGrado() != 0 ? ultimoRegistro.getCicloGrado().getGradoSeccionTurno().getGrado().getNroGrado() : 0)
                .build();
        } catch (Exception e) {
            throw new Exception("Error al crear el alumno: " + e.getMessage());
        }
    }

    // Metodo que traer todos los alumnos con detalles mínimos.
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
                        .ultGrado(ultimoRegistro.getCicloGrado().getGradoSeccionTurno().getGrado().getNroGrado())
                        .seccionGrado(ultimoRegistro.getCicloGrado().getGradoSeccionTurno().getSeccion().getLetra())
                        .turno(ultimoRegistro.getCicloGrado().getGradoSeccionTurno().getTurno().getNombreTurno())
                        .build();
                System.out.println("Testeo del dto: " + alumnoRespuesta);
                alumnosDTO.add(alumnoRespuesta);
            }

            return alumnosDTO;
        } catch (Exception e) {
            throw new Exception(e.getMessage().toString());
        }
    }

    public List<AlumnoInscriptoDTO> listarAlumnosPorCSG(Long idCiclo) {
        try {
            List<RegistroAlumno> registros = registroAlumnoRepository.findByCicloGrado_Id(idCiclo);
            return registros.stream()
                    .map(r -> new AlumnoInscriptoDTO(
                            r.getAlumno().getId(),
                            r.getAlumno().getNombre() + " " + r.getAlumno().getApellido(),
                            r.getAlumno().getDni()
                    ))
                    .toList();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
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

    @Transactional
    public void eliminarAlumno(Long id) throws Exception {
        try {
            Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new Exception("El alumno no existe"));

            // 1. Remover referencias de tutores
            for (Tutor tutor : alumno.getTutores()) {
                tutor.getAlumnos().remove(alumno);
                tutorRepository.save(tutor);
            }
            alumno.getTutores().clear();

            // 2. Remover los registros del alumno (si tienes cascade, esto no es necesario)
            alumno.getRegistroAlumno().clear();
            
            // 3. Guardar el alumno sin sus relaciones
            alumnoRepository.save(alumno);
            
            // 4. Ahora sí podemos eliminar el alumno
            alumnoRepository.delete(alumno);
        } catch (Exception e) {
            throw new Exception("Error al eliminar el alumno: " + e.getMessage());
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

    @Transactional
    public RegistroAlumno crearRegistroAlumno(Long alumnoId, Integer nroGrado, String seccion, String turno, Integer anioCiclo) throws Exception {
        try {
            // Buscar alumno
            Alumno alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new Exception("El alumno no existe"));

            // Buscar o crear grado
            GradoSeccionTurno grado = gradoService.getGradoByNroSeccionTurno(nroGrado, seccion, turno);

            // Buscar o crear ciclo
            CicloGrado cicloGrado = cicloGradoRepository.findByAnio(anioCiclo)
                .orElseGet(() -> {
                    CicloGrado nuevo = new CicloGrado();
                    nuevo.setAnio(anioCiclo);
                    nuevo.setGradoSeccionTurno(grado);
                    return cicloGradoRepository.save(nuevo);
                });

            // Crear registro
            RegistroAlumno registro = new RegistroAlumno();
            registro.setAlumno(alumno);
            registro.setCicloGrado(cicloGrado);
            
            // Agregar registro al alumno
            alumno.getRegistroAlumno().add(registro);
            
            // Solo guardamos el alumno, el registro se guarda en cascada
            alumnoRepository.save(alumno);
            return registro;
        } catch (Exception e) {
            throw new Exception("Error al crear el registro: " + e.getMessage());
        }
    }

    public TutorListaDTO agregarTutor(Long alumnoId, Long tutorId) throws Exception {
        try {
            Alumno alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new Exception("El alumno no existe"));
            Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new Exception("El tutor no existe"));

            if (alumno.getTutores().contains(tutor)) {
                throw new Exception("El tutor ya está asociado al alumno");
            }

            alumno.getTutores().add(tutor);
            tutor.getAlumnos().add(alumno);

            alumnoRepository.save(alumno);
            tutorRepository.save(tutor);

            return TutorListaDTO.builder()
                .id(tutor.getId())
                .nombre(tutor.getNombre())
                .apellido(tutor.getApellido())
                .dni(tutor.getDni())
                .domicilio(tutor.getDomicilio())
                .build();
        } catch (Exception e) {
            throw new Exception("Error al agregar el tutor: " + e.getMessage());
        }
    }
}
