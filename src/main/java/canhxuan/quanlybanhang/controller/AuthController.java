package canhxuan.quanlybanhang.controller;

import canhxuan.quanlybanhang.dto.LoginRequest;
import canhxuan.quanlybanhang.dto.LoginResponse;
import canhxuan.quanlybanhang.dto.RegisterRequest;
import canhxuan.quanlybanhang.entity.User;
import canhxuan.quanlybanhang.repository.UserRepository;
import canhxuan.quanlybanhang.service.email.EmailService;
import canhxuan.quanlybanhang.security.JwtUtils;
import canhxuan.quanlybanhang.service.email.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/quanlybanhang/auth")
public class AuthController {
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private EmailService emailService;
    @Autowired private TokenService emailTokenService;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = new LoginResponse();
        LoginResponse.LoginData data = new LoginResponse.LoginData();
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            String jwtToken =jwtUtils.generateJwtToken(auth);
            String refreshToken =jwtUtils.generateRefreshToken(auth);
            data.setJwtToken(jwtToken);
            data.setRefreshToken(refreshToken);
            data.setTokenType("Bearer");
            data.setExpiresIn(JwtUtils.jwtExpiration);
            data.setUsername(request.getUsername());
            response.setStatus("200");
            response.setMessage("Login Success");
            response.setData(data);
            tokenService.saveAccessToken(jwtToken, request.getUsername(), 5);
            tokenService.saveRefreshToken(refreshToken, request.getUsername(), 60*24*7);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already existed");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setAvatarUrl(request.getAvatarUrl());
        user.setIsEmailVerified(false);
        user.setRole(request.getRole());
        userRepository.save(user);
        String token = UUID.randomUUID().toString();
        emailTokenService.saveToken(user.getUsername(), token, 60*24);
        emailService.send(user.getEmail(), "Verify your email", "Click here to verify your email: http://localhost:8080/quanlybanhang/auth/verify-email?token=" + token);
        return ResponseEntity.ok("Register success, please check your email to verify your account");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null && !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid Authorization Header");
        }
        String token = authHeader.substring(7);
        if (tokenService != null) {
            tokenService.blacklistToken(token, 15);
        }
        return ResponseEntity.ok("Logout success");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        User user = userRepository.findByEmail(email);
        String token = UUID.randomUUID().toString();
        emailTokenService.saveToken(token, user.getUsername(), 60);
        emailService.send(user.getEmail(), "Reset your password", "Click here to reset your password: http://localhost:8080/quanlybanhang/auth/reset-password?token=" + token);
        return ResponseEntity.ok("Please check your email to reset your password");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestBody Map<String, String> request) {
        String username = emailTokenService.getUsernameFromToken(token);
        if (username == null) {
            return ResponseEntity.badRequest().body("Invalid token");
        }
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(request.get("password")));
        userRepository.save(user);
        return ResponseEntity.ok("Reset Password success");
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        String username = emailTokenService.getUsernameFromToken(token);
        System.out.println(username);
        if (username == null) {
            return ResponseEntity.badRequest().body("Invalid Token");
        }
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsEmailVerified(true);
        userRepository.save(user);
        return ResponseEntity.ok("Email verified successfully");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refresh_token");
        String username = jwtUtils.getUsernameFromJwtToken(refreshToken);
        if (!jwtUtils.validateJwtToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
        String newJwtToken = jwtUtils.generateJwtTokenByRefreshToken(refreshToken);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("user not found with username: " + username));
        tokenService.saveAccessToken(newJwtToken, user.getUsername(), 5);
        return ResponseEntity.ok(Map.of("Jwt_token", newJwtToken));
    }

}
