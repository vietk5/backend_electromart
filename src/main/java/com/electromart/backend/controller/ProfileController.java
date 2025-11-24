package com.electromart.backend.controller;

import com.electromart.backend.dto.ApiResponse;
import com.electromart.backend.dto.ChangePasswordRequest;
import com.electromart.backend.dto.UpdateProfileRequest;
import com.electromart.backend.model.KhachHang;
import com.electromart.backend.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProfileController {
    
    private final ProfileService profileService;
    
    /**
     * Lấy thông tin profile theo userId
     * GET /api/profile/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<KhachHang>> getProfile(@PathVariable Long userId) {
        try {
            KhachHang khachHang = profileService.getProfile(userId);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Lấy thông tin thành công", khachHang)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * Lấy thông tin profile theo email
     * GET /api/profile/by-email?email=xxx@gmail.com
     */
    @GetMapping("/by-email")
    public ResponseEntity<ApiResponse<KhachHang>> getProfileByEmail(@RequestParam String email) {
        try {
            KhachHang khachHang = profileService.getProfileByEmail(email);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Lấy thông tin thành công", khachHang)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * Cập nhật thông tin cá nhân
     * PUT /api/profile/{userId}
     */
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<KhachHang>> updateProfile(
            @PathVariable Long userId,
            @RequestBody UpdateProfileRequest request) {
        try {
            KhachHang updatedKhachHang = profileService.updateProfile(userId, request);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Cập nhật thông tin thành công", updatedKhachHang)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
     @PutMapping("/{userId}/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @PathVariable Long userId,
            @RequestBody ChangePasswordRequest request) {
        try {
            profileService.changePassword(userId, request.getNewPassword());
            return ResponseEntity.ok(new ApiResponse<>(true, "Đổi mật khẩu thành công", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}