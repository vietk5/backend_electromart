package com.electromart.backend.dto;

public class ForgotPasswordRequest {
    private String email;
    private String otp; // dùng cho verify-otp

    public ForgotPasswordRequest() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }
}
