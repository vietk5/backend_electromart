package com.electromart.backend.controller;

import com.electromart.backend.dto.AddressDto;
import com.electromart.backend.dto.AddressRequest;
import com.electromart.backend.dto.ApiResponse;
import com.electromart.backend.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AddressController {
    
    private final AddressService addressService;
    
    /**
     * Lấy danh sách địa chỉ của user
     * GET /api/addresses/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<AddressDto>>> getUserAddresses(
            @PathVariable Long userId) {
        
        log.info("REST request to get addresses for user: {}", userId);
        
        try {
            List<AddressDto> addresses = addressService.getUserAddresses(userId);
            
            return ResponseEntity.ok(ApiResponse.<List<AddressDto>>builder()
                    .success(true)
                    .message("Lấy danh sách địa chỉ thành công")
                    .data(addresses)
                    .build());
                    
        } catch (Exception e) {
            log.error("Error getting addresses for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<AddressDto>>builder()
                            .success(false)
                            .message("Lỗi khi lấy danh sách địa chỉ: " + e.getMessage())
                            .build());
        }
    }
    
    /**
     * Lấy chi tiết địa chỉ
     * GET /api/addresses/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressDto>> getAddressById(
            @PathVariable Long id,
            @RequestParam Long userId) {
        
        log.info("REST request to get address by id: {} for user: {}", id, userId);
        
        try {
            AddressDto address = addressService.getAddressById(id, userId);
            
            return ResponseEntity.ok(ApiResponse.<AddressDto>builder()
                    .success(true)
                    .message("Lấy thông tin địa chỉ thành công")
                    .data(address)
                    .build());
                    
        } catch (Exception e) {
            log.error("Error getting address by id: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<AddressDto>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
    
    /**
     * Lấy địa chỉ mặc định của user
     * GET /api/addresses/user/{userId}/default
     */
    @GetMapping("/user/{userId}/default")
    public ResponseEntity<ApiResponse<AddressDto>> getDefaultAddress(
            @PathVariable Long userId) {
        
        log.info("REST request to get default address for user: {}", userId);
        
        try {
            AddressDto address = addressService.getDefaultAddress(userId);
            
            return ResponseEntity.ok(ApiResponse.<AddressDto>builder()
                    .success(true)
                    .message("Lấy địa chỉ mặc định thành công")
                    .data(address)
                    .build());
                    
        } catch (Exception e) {
            log.error("Error getting default address for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<AddressDto>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
    
    /**
     * Tạo địa chỉ mới
     * POST /api/addresses
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AddressDto>> createAddress(
            @Valid @RequestBody AddressRequest request) {
        
        log.info("REST request to create address for user: {}", request.getUserId());
        
        try {
            AddressDto createdAddress = addressService.createAddress(request);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.<AddressDto>builder()
                            .success(true)
                            .message("Thêm địa chỉ thành công")
                            .data(createdAddress)
                            .build());
                            
        } catch (Exception e) {
            log.error("Error creating address", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<AddressDto>builder()
                            .success(false)
                            .message("Lỗi khi thêm địa chỉ: " + e.getMessage())
                            .build());
        }
    }
    
    /**
     * Cập nhật địa chỉ
     * PUT /api/addresses/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressDto>> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressRequest request) {
        
        log.info("REST request to update address id: {} for user: {}", id, request.getUserId());
        
        try {
            AddressDto updatedAddress = addressService.updateAddress(id, request, request.getUserId());
            
            return ResponseEntity.ok(ApiResponse.<AddressDto>builder()
                    .success(true)
                    .message("Cập nhật địa chỉ thành công")
                    .data(updatedAddress)
                    .build());
                    
        } catch (Exception e) {
            log.error("Error updating address id: {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<AddressDto>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
    
    /**
     * Xóa địa chỉ
     * DELETE /api/addresses/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @PathVariable Long id,
            @RequestParam Long userId) {
        
        log.info("REST request to delete address id: {} for user: {}", id, userId);
        
        try {
            addressService.deleteAddress(id, userId);
            
            return ResponseEntity.ok(ApiResponse.<Void>builder()
                    .success(true)
                    .message("Xóa địa chỉ thành công")
                    .build());
                    
        } catch (Exception e) {
            log.error("Error deleting address id: {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<Void>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
    
    /**
     * Đặt địa chỉ làm mặc định
     * PUT /api/addresses/{id}/set-default
     */
    @PutMapping("/{id}/set-default")
    public ResponseEntity<ApiResponse<AddressDto>> setDefaultAddress(
            @PathVariable Long id,
            @RequestParam Long userId) {
        
        log.info("REST request to set default address id: {} for user: {}", id, userId);
        
        try {
            AddressDto updatedAddress = addressService.setDefaultAddress(id, userId);
            
            return ResponseEntity.ok(ApiResponse.<AddressDto>builder()
                    .success(true)
                    .message("Đặt địa chỉ mặc định thành công")
                    .data(updatedAddress)
                    .build());
                    
        } catch (Exception e) {
            log.error("Error setting default address id: {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<AddressDto>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
}
