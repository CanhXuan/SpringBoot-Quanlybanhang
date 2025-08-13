package canhxuan.quanlybanhang.service;

import canhxuan.quanlybanhang.dto.LoginRequest;
import canhxuan.quanlybanhang.dto.LoginResponse;
import canhxuan.quanlybanhang.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface AuthService {
    public LoginResponse login(LoginRequest request);

    public String register(RegisterRequest request);

    public String logout(HttpServletRequest request);

    public String forgotPassword(Map<String, String> request);

    public String resetPassword(String token, Map<String, String> request);

    public String verifyEmail(String token);

    public String refreshToken(Map<String, String> request);
}
