package com.electromart.backend.service;

import com.electromart.backend.dto.RegisterRequestDto;
import com.electromart.backend.dto.ResetPasswordRequest;
import com.electromart.backend.exception.OTPExpiredException;
import com.electromart.backend.model.KhachHang;
import com.electromart.backend.model.NguoiDung;
import com.electromart.backend.repository.NguoiDungRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final NguoiDungRepository repo;

    private final EmailService emailService;

    public KhachHang register(RegisterRequestDto req) {

        // Kiểm tra email duy nhất
        if (repo.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        // Tạo khách hàng mới (KhachHang extends NguoiDung)
        KhachHang user = new KhachHang();
        user.setHoTen(req.getHoTen());
        user.setEmail(req.getEmail());
        user.setMatKhau(req.getMatKhau());

        return repo.save(user);
    }
    public KhachHang login(String email, String matKhau) {
        KhachHang user = (KhachHang) repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

        if (!user.getMatKhau().equals(matKhau)) {
            throw new RuntimeException("Sai mật khẩu!");
        }

        return user;
    }

    public void sendForgotPasswordEmail(String email) {
        KhachHang user = (KhachHang) repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

        String otp = generateOTP();
        user.setOtp(otp);
        repo.save(user);

        emailService.sendOtpEmail(user.getEmail(), otp);
    }

    private String generateOTP() {
        // Generate a random 6-digit OTP
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void verifyOTP(String email, String otp) {
        KhachHang user = (KhachHang) repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

        if (!user.getOtp().equals(otp)) {
            throw new OTPExpiredException("Invalid OTP");
        }
        
    }
    public void resetPassword(ResetPasswordRequest request) {
        KhachHang user = (KhachHang) repo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

        // Update user's password
        user.setMatKhau(request.getNewPass());
        repo.save(user);
    }

}
