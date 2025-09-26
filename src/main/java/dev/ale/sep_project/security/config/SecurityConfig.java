package dev.ale.sep_project.security.config;

import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import dev.ale.sep_project.models.Token;
import dev.ale.sep_project.repository.TokenRepository;
import dev.ale.sep_project.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;
    private final TokenRepository tokenRepository;

    // Acá se reestringen las rutas y se configuran los filtros de seguridad.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authRequest -> 
                authRequest
                    .requestMatchers("/auth/**").permitAll()
                    .anyRequest().authenticated()
                    )
            .sessionManagement(sessionManager ->
                sessionManager
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .logout(logout -> 
                logout.logoutUrl("/auth/logout")
                    .addLogoutHandler((request, response, authentication) -> {
                        final String authHeader = request.getHeader("Authorization");
                        logout(authHeader);
                    })
                    .logoutSuccessHandler((request, response, authentication) -> 
                        SecurityContextHolder.clearContext()
                    ))
            .build();
    }

    private void logout(final String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("Token inválido o ausente");
        }
        final String jwt = token.substring(7);
        final Token tokenEncontrado = tokenRepository.findByToken(jwt)
            .orElseThrow(() -> new RuntimeException("Token no encontrado"));
        
        tokenEncontrado.setRevoked(true);
        tokenEncontrado.setExpired(true);
        tokenRepository.save(tokenEncontrado);
    }
}
