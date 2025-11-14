package com.electromart.backend.controller;

import com.electromart.backend.dto.LoginRequestDto;
import com.electromart.backend.dto.LoginResponseDto;
import com.electromart.backend.dto.RegisterResponseDto;
import com.electromart.backend.dto.RegisterRequestDto;
import com.electromart.backend.model.KhachHang;
import com.electromart.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public RegisterResponseDto<KhachHang> register(@RequestBody RegisterRequestDto req) {
        try {
            KhachHang kh = authService.register(req);
            return new RegisterResponseDto<>(true, "Đăng ký thành công!", kh);
        } catch (Exception e) {
            return new RegisterResponseDto<>(false, e.getMessage(), null);
        }
    }
    @PostMapping("/login")
    public LoginResponseDto<KhachHang> login(@RequestBody LoginRequestDto req) {
        try {
            KhachHang kh = authService.login(req.getEmail(), req.getMatKhau());
            return new LoginResponseDto<>(true, "Đăng nhập thành công!", kh);
        } catch (Exception e) {
            return new LoginResponseDto<>(false, e.getMessage(), null);
        }
    }
}
