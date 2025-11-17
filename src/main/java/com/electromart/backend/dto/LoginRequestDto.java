package com.electromart.backend.dto;

public class LoginRequestDto {
    private String email;
    private String matKhau;

    public LoginRequestDto() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
}
