package canhxuan.quanlybanhang.controller;

import canhxuan.quanlybanhang.dto.LoginRequest;
import canhxuan.quanlybanhang.dto.LoginResponse;
import canhxuan.quanlybanhang.dto.RegisterRequest;
import canhxuan.quanlybanhang.entity.EmailVerificationToken;
import canhxuan.quanlybanhang.entity.PasswordResetToken;
import canhxuan.quanlybanhang.entity.Token;
import canhxuan.quanlybanhang.entity.User;
import canhxuan.quanlybanhang.repository.EmailVerificationTokenRepository;
import canhxuan.quanlybanhang.repository.PasswordResetTokenRepository;
import canhxuan.quanlybanhang.repository.TokenRepository;
import canhxuan.quanlybanhang.repository.UserRepository;
import canhxuan.quanlybanhang.security.EmailService;
import canhxuan.quanlybanhang.security.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/quanlybanhang/auth")
public class AuthController {
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private TokenRepository tokenRepository;
    @Autowired private EmailVerificationTokenRepository emailVerificationTokenRepository;
    @Autowired private EmailService emailService;
    @Autowired private PasswordResetTokenRepository passwordResetTokenRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = new LoginResponse();
        LoginResponse.LoginData data = new LoginResponse.LoginData();
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
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
            tokenRepository.save(new Token(jwtToken, false, false, user));
            tokenRepository.save(new Token(refreshToken, false, false, user));
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
        EmailVerificationToken verificationToken = new EmailVerificationToken(token, LocalDateTime.now().plusHours(24), user);
        emailVerificationTokenRepository.save(verificationToken);
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
        Token storedToken = tokenRepository.findByToken(token).orElse(null);
        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
        }
        return ResponseEntity.ok("Logout success");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        User user = userRepository.findByEmail(email);
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, LocalDateTime.now().plusHours(1), user);
        passwordResetTokenRepository.save(passwordResetToken);
        emailService.send(user.getEmail(), "Reset your password", "Click here to reset your password: http://localhost:8080/quanlybanhang/auth/reset-password?token=" + token);
        return ResponseEntity.ok("Please check your email to reset your password");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestBody Map<String, String> request) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Token invalid"));
        if (passwordResetToken.getExpiration().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Token is expired");
        }
        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.get("password")));
        userRepository.save(user);
        return ResponseEntity.ok("Reset Password success");
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        EmailVerificationToken emailToken = emailVerificationTokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Email verification token not found with token: " + token));
        if (emailToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Email verification token is expired");
        }
        User user = emailToken.getUser();
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
        tokenRepository.save(new Token(newJwtToken, false, false, user));
        return ResponseEntity.ok(Map.of("Jwt_token", newJwtToken));
    }

}
