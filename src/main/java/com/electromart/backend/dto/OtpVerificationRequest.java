package com.electromart.backend.dto;

import lombok.Data;

@Data
public class OtpVerificationRequest {
    private String email;
    private String otp;
}
