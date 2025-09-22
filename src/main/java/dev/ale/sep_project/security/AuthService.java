package dev.ale.sep_project.security;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.ale.sep_project.models.Maestro;
import dev.ale.sep_project.models.Rol;
import dev.ale.sep_project.models.Usuario;
import dev.ale.sep_project.repository.MaestroRepository;
import dev.ale.sep_project.repository.UsuarioRepository;
import dev.ale.sep_project.security.dto.AuthResponse;
import dev.ale.sep_project.security.dto.LoginRequest;
import dev.ale.sep_project.security.dto.RegisterRequest;
import dev.ale.sep_project.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MaestroRepository maestroRepository;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            
            UserDetails user = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            
            String token = jwtService.getToken(user);
            return AuthResponse.builder()
                .token(token)
                .build();
                
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Credenciales inválidas");
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("Usuario no encontrado");
        } catch (Exception e) {
            throw new RuntimeException("Error durante la autenticación: " + e.getMessage());
        }
    }

    public AuthResponse register(RegisterRequest request) {
        
        // Configurar despeus
        // if (!maestroRepository.findByDni(request.getDni()).isPresent()) {
        //     throw new Exception("El DNI que estás pasando ya existe en el sistema");
        // }
        // Maestro maestro = new Maestro()

        Usuario user = Usuario.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .rol(Rol.USUARIO)
            .build();
        
        usuarioRepository.save(user);

        return AuthResponse.builder()
            .token(jwtService.getToken(user))
            .build();
    }

}
