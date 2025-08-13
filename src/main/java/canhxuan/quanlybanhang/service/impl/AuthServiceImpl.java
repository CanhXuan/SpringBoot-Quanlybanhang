package canhxuan.quanlybanhang.service.impl;

import canhxuan.quanlybanhang.dto.EmailRequest;
import canhxuan.quanlybanhang.dto.LoginRequest;
import canhxuan.quanlybanhang.dto.LoginResponse;
import canhxuan.quanlybanhang.dto.RegisterRequest;
import canhxuan.quanlybanhang.entity.User;
import canhxuan.quanlybanhang.repository.UserRepository;
import canhxuan.quanlybanhang.service.AuthService;
import canhxuan.quanlybanhang.service.email.EmailProducer;
import canhxuan.quanlybanhang.service.email.EmailService;
import canhxuan.quanlybanhang.service.email.TokenService;
import canhxuan.quanlybanhang.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private EmailProducer emailProducer;

    @Override
    public LoginResponse login(LoginRequest request) {
        LoginResponse response = new LoginResponse();
        LoginResponse.LoginData data = new LoginResponse.LoginData();
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            String jwtToken = jwtUtils.generateJwtToken(auth);
            String refreshToken = jwtUtils.generateRefreshToken(auth);
            data.setJwtToken(jwtToken);
            data.setRefreshToken(refreshToken);
            data.setTokenType("Bearer");
            data.setExpiresIn(JwtUtils.jwtExpiration);
            data.setUsername(request.getUsername());
            response.setStatus("200");
            response.setMessage("Login Success");
            response.setData(data);
            tokenService.saveAccessToken(jwtToken, request.getUsername(), 5);
            tokenService.saveRefreshToken(refreshToken, request.getUsername(), 60 * 24 * 7);
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return "Username is already existed";
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setAvatarUrl(request.getAvatarUrl());
        user.setIsEmailVerified(false);
        user.setRole("ADMIN");
        userRepository.save(user);
        String token = UUID.randomUUID().toString();
        System.out.println(user.getUsername());
        EmailRequest emailRequest = new EmailRequest(user.getEmail(), "Verify your email", "Click here to verify your email: http://localhost:8080/quanlybanhang/auth/verify-email?token=" + token);
        tokenService.saveToken(token, user.getUsername(), 60 * 24);
//        emailService.send(user.getEmail(), );
        try {
            String emailJson = new ObjectMapper().writeValueAsString(emailRequest);
            emailProducer.sendEmailEvent(emailJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Register success, please check your email to verify your account";
    }

    @Override
    public String logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return "Invalid Authorization Header";
        }
        String token = authHeader.substring(7);
        tokenService.blacklistToken(token, 15);
        return "Logout success";
    }

    @Override
    public String forgotPassword(Map<String, String> request) {
        String email = request.get("email");
        User user = userRepository.findByEmail(email);
        String token = UUID.randomUUID().toString();
        tokenService.saveToken(token, user.getUsername(), 60);
        emailService.send(user.getEmail(), "Reset your password", "Click here to reset your password: http://localhost:8080/quanlybanhang/auth/reset-password?token=" + token);
        return "Please check your email to reset your password";
    }

    @Override
    public String resetPassword(String token, Map<String, String> request) {
        String username = tokenService.getUsernameFromToken(token);
        if (username == null) {
            return "Invalid token";
        }
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(request.get("password")));
        userRepository.save(user);
        return "Reset Password success";
    }

    @Override
    public String verifyEmail(String token) {
        String username = tokenService.getUsernameFromToken(token);
        System.out.println(username);
        if (username == null) {
            return "Invalid Token";
        }
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsEmailVerified(true);
        userRepository.save(user);
        return "Email verified successfully";
    }

    @Override
    public String refreshToken(Map<String, String> request) {
        String refreshToken = request.get("refresh_token");
        String username = jwtUtils.getUsernameFromJwtToken(refreshToken);
        if (!jwtUtils.validateJwtToken(refreshToken)) {
            return "Invalid token";
        }
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        String newJwtToken = jwtUtils.generateJwtTokenByRefreshToken(refreshToken);
        tokenService.saveAccessToken(newJwtToken, user.getUsername(), 5);
        return newJwtToken;
    }
}
