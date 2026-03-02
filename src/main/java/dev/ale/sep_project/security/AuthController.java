package dev.ale.sep_project.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import dev.ale.sep_project.security.dto.AuthResponse;
import dev.ale.sep_project.security.dto.LoginRequest;
import dev.ale.sep_project.security.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.util.Arrays;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final CookieService cookieService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", authResponse.getRefreshToken())
                .httpOnly(true)
                .secure(false) // ⚠️ en local, debe ser false
                .sameSite("Lax") // más permisivo en desarrollo
                .path("/auth")
                .maxAge(Duration.ofDays(7))
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(AuthResponse.builder()
                        .token(authResponse.getToken())
                        .build());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request) {
        System.out.println("REFRESH ENDPOINT EJECUTADO");
        System.out.println(Arrays.toString(request.getCookies()));
        String refreshToken = cookieService.getRefreshToken(request);

        System.out.println("refresh token: " + refreshToken);
        AuthResponse authResponse = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(AuthResponse.builder()
                .token(authResponse.getToken())
                .build());
    }

    @PreAuthorize("hasAnyRole('ADMIN','DIRECTOR')")
    @GetMapping("/auth-debug")
    public ResponseEntity<?> debug() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authorities: " + auth.getAuthorities());
        return ResponseEntity.ok(auth);
    }
}
