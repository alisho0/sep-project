package dev.ale.sep_project.security.jwt;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

                String path = request.getServletPath();

                if (path.equals("/auth/login") ||
                        path.equals("/auth/logout") ||
                        path.equals("/auth/refresh")) {
                    filterChain.doFilter(request, response);
                    return;
                }

                final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    filterChain.doFilter(request, response);
                    return;

                }

                final String jwtToken = authHeader.substring(7);
                String username;

                try {
                    username = jwtService.extractUsername(jwtToken);
                } catch (ExpiredJwtException e) {
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

                Claims claims = jwtService.extractAllClaims(jwtToken);

                String rol = usuario.get().getRol().name();
                List<GrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_" + rol)
                );
                final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        authorities);

                authToken.setDetails((claims));

                // Esto es para más tarde, juntar a claims y esto junto.
                // authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                filterChain.doFilter(request, response);
    }
}
