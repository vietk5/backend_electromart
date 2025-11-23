package com.electromart.backend.controller;

import com.electromart.backend.dto.*;
import com.electromart.backend.model.KhachHang;
import com.electromart.backend.repository.NguoiDungRepository;
import com.electromart.backend.service.AuthService;
import com.electromart.backend.service.EmailService;
import com.electromart.backend.exception.OTPExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final NguoiDungRepository nguoiDungRepository;
    private final AuthService authService;
    private final EmailService emailService;

    // ------------------ REGISTER ------------------
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequestDto req) {
    try {
        // Gọi method đăng ký trên AuthService với DTO
        KhachHang kh = authService.register(req); 
        // Gửi email xác nhận đăng ký
        emailService.sendRegisterEmail(kh);
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Đăng ký thành công! Email xác nhận đã gửi.", kh));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, e.getMessage(), null));
    }
}


    // ------------------ LOGIN ------------------
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequestDto req) {
        try {
            KhachHang kh = authService.login(req.getEmail(), req.getMatKhau()); 
            return ResponseEntity.ok(new ApiResponse<>(true, "Đăng nhập thành công!", kh));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // ------------------ FORGOT PASSWORD ------------------
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody ForgotPasswordRequest req) {
        try {
            authService.sendForgotPasswordEmail(req.getEmail());
            return ResponseEntity.ok(new ApiResponse<>(true, "OTP đã được gửi tới email của bạn", null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Đã xảy ra lỗi", null));
        }
    }

    // ------------------ VERIFY OTP ------------------
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse> verifyOTP(@RequestBody ForgotPasswordRequest req) {
        try {
            authService.verifyOTP(req.getEmail(), req.getOtp());
            return ResponseEntity.ok(new ApiResponse<>(true, "OTP xác thực thành công", null));
        } catch (OTPExpiredException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "OTP không hợp lệ hoặc đã hết hạn", null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Đã xảy ra lỗi", null));
        }
    }

    // ------------------ RESET PASSWORD ------------------
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPasswordRequest req) {
        try {
            authService.resetPassword(req);
            return ResponseEntity.ok(new ApiResponse<>(true, "Mật khẩu đã được thay đổi thành công", null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Đã xảy ra lỗi", null));
        }
    }
}
