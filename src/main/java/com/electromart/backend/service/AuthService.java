package com.electromart.backend.service;

import com.electromart.backend.dto.RegisterRequestDto;
import com.electromart.backend.model.KhachHang;
import com.electromart.backend.repository.NguoiDungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final NguoiDungRepository repo;

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

}
