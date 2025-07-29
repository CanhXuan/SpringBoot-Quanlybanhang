package canhxuan.quanlybanhang.dto;

import lombok.Data;

@Data
public class ApiResponse <T>{
    private int code;
    private String message;
    private T result;
}
