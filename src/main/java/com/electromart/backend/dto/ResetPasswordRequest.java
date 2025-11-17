package com.electromart.backend.dto;

public class ResetPasswordRequest {
    private String email;
    private String newPass;

    public ResetPasswordRequest() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNewPass() { return newPass; }
    public void setNewPass(String newPass) { this.newPass = newPass; }
}
