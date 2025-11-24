package com.electromart.backend.service;

import com.electromart.backend.dto.UpdateProfileRequest;
import com.electromart.backend.model.KhachHang;
import com.electromart.backend.model.NguoiDung;
import com.electromart.backend.repository.NguoiDungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    
    private final NguoiDungRepository nguoiDungRepository;
    
    /**
     * Lấy thông tin user theo ID
     */
    public KhachHang getProfile(Long userId) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        
        if (!(nguoiDung instanceof KhachHang)) {
            throw new RuntimeException("Người dùng không phải là khách hàng");
        }
        
        return (KhachHang) nguoiDung;
    }
    
    /**
     * Lấy thông tin user theo email
     */
    public KhachHang getProfileByEmail(String email) {
        NguoiDung nguoiDung = nguoiDungRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        
        if (!(nguoiDung instanceof KhachHang)) {
            throw new RuntimeException("Người dùng không phải là khách hàng");
        }
        
        return (KhachHang) nguoiDung;
    }
    
    /**
     * Cập nhật thông tin cá nhân
     */
    @Transactional
    public KhachHang updateProfile(Long userId, UpdateProfileRequest request) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        
        if (!(nguoiDung instanceof KhachHang)) {
            throw new RuntimeException("Người dùng không phải là khách hàng");
        }
        
        KhachHang khachHang = (KhachHang) nguoiDung;
        
        // Cập nhật các trường thông tin
        if (request.getHoTen() != null && !request.getHoTen().trim().isEmpty()) {
            khachHang.setHoTen(request.getHoTen().trim());
        }
        
        if (request.getSoDienThoai() != null && !request.getSoDienThoai().trim().isEmpty()) {
            // Validate số điện thoại (tuỳ chọn)
            String sdt = request.getSoDienThoai().trim();
            if (!sdt.matches("^[0-9]{10,11}$")) {
                throw new RuntimeException("Số điện thoại không hợp lệ");
            }
            khachHang.setSoDienThoai(sdt);
        }
        
        // Lưu vào database
        return nguoiDungRepository.save(khachHang);
    }
    
    /**
     * Đổi mật khẩu (không cần mật khẩu cũ)
     */
    @Transactional
    public void changePassword(Long userId, String newPassword) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // Xác thực mật khẩu mới
        if (newPassword == null || newPassword.length() < 6) {
            throw new RuntimeException("Mật khẩu mới phải có ít nhất 6 ký tự");
        }

        // Cập nhật mật khẩu
        nguoiDung.setMatKhau(newPassword);
        nguoiDungRepository.save(nguoiDung);
    }
}