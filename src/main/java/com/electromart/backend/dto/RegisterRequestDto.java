package com.electromart.backend.dto;

public class RegisterRequestDto {
    private String email;
    private String matKhau;
    private String hoTen;

    public RegisterRequestDto() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
}
