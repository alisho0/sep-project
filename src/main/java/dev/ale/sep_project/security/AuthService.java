package dev.ale.sep_project.security;

import java.util.List;

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
import dev.ale.sep_project.models.Token;
import dev.ale.sep_project.models.Usuario;
import dev.ale.sep_project.models.Token.TokenType;
import dev.ale.sep_project.repository.MaestroRepository;
import dev.ale.sep_project.repository.TokenRepository;
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
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            
            Usuario user = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            
            String token = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, token);
            return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
                
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Credenciales inválidas");
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("Usuario no encontrado");
        } catch (Exception e) {
            throw new RuntimeException("Error durante la autenticación: " + e.getMessage());
        }
    }

    private void revokeAllUserTokens(Usuario user) {
        final List<Token> validUserTokens = tokenRepository.findAllValidIsFalseOrRevokedIsFalseByUsuario_Id(user.getId());
        if (!validUserTokens.isEmpty()) {

            validUserTokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });
            tokenRepository.saveAll(validUserTokens);
        }
    }

    public AuthResponse register(RegisterRequest request) {
        
        
        if (!maestroRepository.findByDni(request.getDni()).isPresent()) {
            throw new RuntimeException("El DNI que estás pasando ya existe en el sistema");
        }
        Maestro maestro = new Maestro();
        maestro.setDni(request.getDni());
        maestro.setNombre(request.getNombre());
        maestro.setApellido(request.getApellido());
        maestro.setDomicilio(request.getDomicilio());
        maestroRepository.save(maestro);

        Usuario user = Usuario.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .rol(Rol.USUARIO)
            .build();
        
        Usuario usuarioGuardado = usuarioRepository.save(user);
        String jwtToken = jwtService.generateToken(usuarioGuardado);
        String refreshToken = jwtService.generateRefreshToken(usuarioGuardado);
        saveUserToken(usuarioGuardado, jwtToken);
        return AuthResponse.builder()
            .token(jwtToken)
            .refreshToken(refreshToken)
            .build();
    }

    public void saveUserToken(Usuario user, String jwtToken) {
        Token token = Token.builder()
            .usuario(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();   
        tokenRepository.save(token);
    }

    public AuthResponse refreshToken(final String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("El encabezado de autorización es inválido");
        }
        String refreshToken = authHeader.substring(7);
        String username = jwtService.extractUsername(refreshToken);

        if (username == null) {
            throw new IllegalArgumentException("El token es inválido");
        }

        Usuario user = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        
        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new IllegalArgumentException("El token de actualización no es válido");
        }
        
        String newAccessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, newAccessToken);
        return AuthResponse.builder()
            .token(newAccessToken)
            .refreshToken(refreshToken)
            .build();
    }

}
