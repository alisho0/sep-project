package dev.ale.sep_project.security;

import org.springframework.stereotype.Service;

import dev.ale.sep_project.security.dto.AuthResponse;
import dev.ale.sep_project.security.dto.LoginRequest;
import dev.ale.sep_project.security.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    

    public AuthResponse login(LoginRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'login'");
    }

    public AuthResponse register(RegisterRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'register'");
    }

}
