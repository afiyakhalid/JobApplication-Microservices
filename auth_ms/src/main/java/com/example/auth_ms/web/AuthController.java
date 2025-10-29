package com.example.auth_ms.web;
import com.example.auth_ms.config.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtUtils jwtUtils;

    public AuthController(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    record LoginRequest(String username, String password) {
    }

    record LoginResponse(String accessToken) {
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        if (("afiya".equals(req.username()) && "pass".equals(req.password())) ||
                ("demo".equals(req.username()) && "demo".equals(req.password()))) {
            return new LoginResponse(jwtUtils.generate(req.username()));
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
    }

    @PostMapping("/validate")
    public Map<String, String> validate(@RequestHeader("Authorization") String hdr) {
        String token = hdr.replace("Bearer ", "");
        if (jwtUtils.validate(token)) {
            return Map.of("username", jwtUtils.getUsername(token));
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
    }
}