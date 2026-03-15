package br.com.lox.domain.auth.controller;

import br.com.lox.config.security.JwtService;
import br.com.lox.config.security.LoginRateLimiter;
import br.com.lox.domain.auth.dto.LoginDTO;
import br.com.lox.domain.auth.dto.TokenDTO;
import br.com.lox.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final LoginRateLimiter rateLimiter;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          JwtService jwtService, LoginRateLimiter rateLimiter) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.rateLimiter = rateLimiter;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO data, HttpServletRequest request) {
        String ip = getClientIp(request);

        if (rateLimiter.isBlocked(ip)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }

        var user = userRepository.findByEmail(data.email()).orElse(null);

        if (user == null || !passwordEncoder.matches(data.senha(), user.getSenha())) {
            rateLimiter.registerFailure(ip);
            throw new BadCredentialsException("E-mail ou senha incorretos");
        }

        rateLimiter.resetAttempts(ip);
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new TokenDTO(token, user.getNome()));
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
