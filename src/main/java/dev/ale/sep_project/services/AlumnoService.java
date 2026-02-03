package dev.ale.sep_project.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import dev.ale.sep_project.dtos.alumnos.*;
import dev.ale.sep_project.exceptions.BusinessLogicException;
import dev.ale.sep_project.exceptions.ResourceAlreadyExistsException;
import dev.ale.sep_project.exceptions.ResourceNotFoundException;
import dev.ale.sep_project.models.*;
import dev.ale.sep_project.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public AlumnoResponseDTO crearAlumno(AlumnoCreateDTO alumnoDto) {
        if (alumnoRepository.findByDni(alumnoDto.getDni()).isPresent()) {
            throw new ResourceAlreadyExistsException("Ya existe un alumno con el DNI: " + alumnoDto.getDni());
        }
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
                    throw new ResourceNotFoundException("Alguna discapacidad no existe");
                }

                alumno.setDiscapacidades(discapacidades);
            }


            // Inicializa la lista de tutores si es null
            if (alumno.getTutores() == null) {
                alumno.setTutores(new ArrayList<>());
            }

            for (Long tutorId : alumnoDto.getTutoresIds()) {
                Tutor tutor = tutorRepository.findById(tutorId)
                        .orElseThrow(() -> new ResourceNotFoundException("Tutor", tutorId));
                alumno.getTutores().add(tutor);
            }
            
            // Finalmente el registro que referencia tanto al alumno como al ciclo grado

            // 1. Partimos por el registro
            RegistroAlumno primerRegistro = new RegistroAlumno();

            if (!gradoService.existeGrado(alumnoDto.getNroGrado(), alumnoDto.getSeccionGrado(), alumnoDto.getTurnoGrado())) {
                throw new ResourceNotFoundException("No existe el grado enviado");
            }
            // traemos el grado
            GradoSeccionTurno grado = gradoService.getGradoByNroSeccionTurno(alumnoDto.getNroGrado(), alumnoDto.getSeccionGrado(), alumnoDto.getTurnoGrado());

            if (!cicloGradoService.existeCicloGrado(alumnoDto.getAnioCicloGrado(), grado)) {
                throw new ResourceNotFoundException("No existe el ciclo grado enviado");
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
                .orElseThrow(() -> new ResourceNotFoundException("No se encontraron registros"));

            return AlumnoResponseDTO.builder()
                .id(alumno.getId())
                .nombre(alumno.getNombre())
                .apellido(alumno.getApellido())
                .dni(alumno.getDni())
                .seccionGrado(ultimoRegistro.getCicloGrado().getGradoSeccionTurno().getSeccion().getLetra() != null ? ultimoRegistro.getCicloGrado().getGradoSeccionTurno().getSeccion().getLetra() : "Sin sección")
                .turno(ultimoRegistro.getCicloGrado().getGradoSeccionTurno().getTurno().getNombreTurno() != null ? ultimoRegistro.getCicloGrado().getGradoSeccionTurno().getTurno().getNombreTurno() : "Sin turno")
                .ultGrado(ultimoRegistro.getCicloGrado().getGradoSeccionTurno().getGrado().getNroGrado() != 0 ? ultimoRegistro.getCicloGrado().getGradoSeccionTurno().getGrado().getNroGrado() : 0)
                .build();
    }

    // Metodo que traer todos los alumnos con detalles mínimos.
    public List<AlumnoResponseDTO> obtenerAlumnos() throws Exception {
        try {
            List<Alumno> alumnos = (List<Alumno>) alumnoRepository.findAll();
            //System.out.println("Alumnos encontrados: " + alumnos.size());
            List<AlumnoResponseDTO> alumnosDTO = new ArrayList<>();

            for (Alumno alu : alumnos) {
                //System.out.println("El alumno es: " + alu.getNombre() + " " + alu.getApellido());
                RegistroAlumno ultimoRegistro = alu.getRegistroAlumno().stream()
                        .sorted(Comparator.comparing(RegistroAlumno::getFechaInicio).reversed())
                        .findFirst()
                        .orElse(null);
//                System.out.println("El último registro es: " + ultimoRegistro.getFechaInicio());
                AlumnoResponseDTO alumnoRespuesta = AlumnoResponseDTO.builder()
                        .id(alu.getId())
                        .nombre(alu.getNombre())
                        .apellido(alu.getApellido())
                        .dni(alu.getDni())
                        .ultGrado(ultimoRegistro != null ? ultimoRegistro.getCicloGrado().getGradoSeccionTurno().getGrado().getNroGrado() : 0)
                        .seccionGrado(ultimoRegistro != null ? ultimoRegistro.getCicloGrado().getGradoSeccionTurno().getSeccion().getLetra() : "Sin registrar")
                        .turno(ultimoRegistro != null ? ultimoRegistro.getCicloGrado().getGradoSeccionTurno().getTurno().getNombreTurno(): "Sin registrar")
                        .build();
//                System.out.println("Testeo del dto: " + alumnoRespuesta);
                alumnosDTO.add(alumnoRespuesta);
            }

            return alumnosDTO;
        } catch (Exception e) {
            throw new Exception(e.getMessage().toString());
        }
    }

    public List<AlumnoInscriptoDTO> listarAlumnosPorCSG(Long idCiclo) {

        List<RegistroAlumno> registros = registroAlumnoRepository.findByCicloGrado_Id(idCiclo);
        if (registros.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron alumnos para el ciclo con ID " + idCiclo);
        }

        return registros.stream()
                .map(r -> new AlumnoInscriptoDTO(
                        r.getAlumno().getId(),
                        r.getAlumno().getNombre() + " " + r.getAlumno().getApellido(),
                        r.getAlumno().getDni(),
                        r.getId()
                ))
                .toList();

    }

    // Obtiene el detalle de UN alumno
    public AlumnoDetalleDTO obtenerAlumno(Long id) {
        Alumno alumno = alumnoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Alumno", id));
        return AlumnoDetalleDTO.builder()
                .id(alumno.getId())
                .nombre(alumno.getNombre())
                .apellido(alumno.getApellido())
                .dni(alumno.getDni())
                .domicilio(alumno.getDomicilio())
                .discapacidades(alumno.getDiscapacidades().stream()
                        .map(Discapacidad::getNombre)
                        .toList())
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

    public List<AlumnoResponseDTO> searchAlumnos(String query) {
        Pageable pageable = PageRequest.of(0, 10); // primera página, 10 resultados
        Page<Alumno> page = alumnoRepository.findByNombreContainingIgnoreCaseOrDniContaining(query, query, pageable);
        if (page.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron alumnos con la solicitud ingresada.");
        }
        return page.stream()
                .map(a -> {
                    RegistroAlumno ultimoRegistro = a.getRegistroAlumno().stream()
                            .sorted(Comparator.comparing(RegistroAlumno::getFechaInicio).reversed())
                            .findFirst()
                            .orElse(null);
                    return AlumnoResponseDTO.builder()
                            .id(a.getId())
                            .nombre(a.getNombre())
                            .apellido(a.getApellido())
                            .dni(a.getDni())
                            .ultGrado(ultimoRegistro != null ? ultimoRegistro.getCicloGrado().getGradoSeccionTurno().getGrado().getNroGrado() : 0)
                            .seccionGrado(ultimoRegistro != null ? ultimoRegistro.getCicloGrado().getGradoSeccionTurno().getSeccion().getLetra() : "Sin registrar")
                            .turno(ultimoRegistro != null ? ultimoRegistro.getCicloGrado().getGradoSeccionTurno().getTurno().getNombreTurno(): "Sin registrar")
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void eliminarAlumno(Long id) {
            Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno", id));

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
    }

    // Creo que puedo utilizar el DTO de creación, tiene la misma estructura, solo
    // habría que sacarle la lista de tutores, o adaptarla para no mandar nada
    public void actualizarAlumno(Long id, AlumnoUpdateDTO alumnoDto) {
            Alumno alumno = alumnoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Alumno", id));
            alumno.setNombre(alumnoDto.getNombre());
            alumno.setApellido(alumnoDto.getApellido());
            alumno.setDiscapacidad(alumnoDto.getDiscapacidad());
            alumno.setDetalleDiscap(alumnoDto.getDetalleDiscap());
            alumno.setDomicilio(alumnoDto.getDomicilio());
            alumno.setDni(alumnoDto.getDni());
            alumnoRepository.save(alumno);
    }

    @Transactional
    public RegistroAlumno crearRegistroAlumno(Long alumnoId, Integer nroGrado, String seccion, String turno, Integer anioCiclo) {
            // Buscar alumno
            Alumno alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno", alumnoId));

            // Buscar o crear grado
            GradoSeccionTurno grado = gradoService.getGradoByNroSeccionTurno(nroGrado, seccion, turno);
            if (grado == null) {
                throw new ResourceNotFoundException("Grado no encontrado");
            }
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
    }

    public TutorListaDTO agregarTutor(Long alumnoId, Long tutorId) {

        Alumno alumno = alumnoRepository.findById(alumnoId).orElseThrow(() -> new ResourceNotFoundException("Alumno", alumnoId));
        Tutor tutor = tutorRepository.findById(tutorId).orElseThrow(() -> new ResourceNotFoundException("Tutor", tutorId));

        if (alumno.getTutores().contains(tutor)) {
            throw new ResourceAlreadyExistsException("El tutor ya está asociado al alumno");
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
    }
}
