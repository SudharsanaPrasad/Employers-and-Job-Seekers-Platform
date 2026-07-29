package com.example.careernest.service;

import com.example.careernest.document.User;
import com.example.careernest.dto.AuthResponse;
import com.example.careernest.dto.LoginRequest;
import com.example.careernest.dto.RegisterRequest;
import com.example.careernest.exception.DuplicateResourceException;
import com.example.careernest.exception.InvalidCredentialsException;
import com.example.careernest.repository.UserRepository;
import com.example.careernest.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SmsService smsService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email is already registered: " + request.email());
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setPhone(request.phone());
        user.setRole(request.role());
        userRepository.save(user);

        // registration confirmation SMS (skipped/logged if Twilio is not configured)
        smsService.send(user.getPhone(),
                "Welcome to CareerNest, " + user.getName() + "! Your account is ready as a "
                        + user.getRole().name().replace("_", " ").toLowerCase() + ".");

        return buildResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return buildResponse(user);
    }

    private AuthResponse buildResponse(User user) {
        String token = jwtService.generateToken(user.getId());
        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
