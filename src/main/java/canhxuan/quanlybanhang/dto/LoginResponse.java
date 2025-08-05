package canhxuan.quanlybanhang.dto;

import canhxuan.quanlybanhang.entity.User;
import lombok.Data;

@Data
public class LoginResponse {
    private String status;
    private String message;
    private LoginData data;

    @Data
    public static class LoginData{
        private String username;
        private String jwtToken;
        private String refreshToken;
        private String tokenType;
        private int expiresIn;
    }
}
