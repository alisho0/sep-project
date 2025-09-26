package dev.ale.sep_project.security.jwt;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import dev.ale.sep_project.models.Token;
import dev.ale.sep_project.models.Usuario;
import dev.ale.sep_project.repository.TokenRepository;
import dev.ale.sep_project.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final UsuarioRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

                if (request.getServletPath().startsWith("/auth/")) {
                    filterChain.doFilter(request, response);
                    return;
                }

                final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    filterChain.doFilter(request, response);
                    return;
                    
                }

                final String jwtToken = authHeader.substring(7);
                final String username = jwtService.extractUsername(jwtToken);

                if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
                    filterChain.doFilter(request, response);
                    return;
                }

                final Token token = tokenRepository.findByToken(jwtToken)
                    .orElse(null);

                if (token == null || token.isExpired() || token.isRevoked()) {
                    filterChain.doFilter(request, response);
                    return;
                }

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                final Optional<Usuario> usuario = userRepository.findByUsername(username);
                
                if (usuario.isEmpty()) {
                    filterChain.doFilter(request, response);
                    return;
                }

                final boolean isTokenValid = jwtService.isTokenValid(jwtToken, usuario.get());

                if (!isTokenValid) {
                    filterChain.doFilter(request, response);
                    return;
                }

                final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                filterChain.doFilter(request, response);
    }
}
