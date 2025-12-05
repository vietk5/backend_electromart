package com.electromart.backend.service;

import com.electromart.backend.dto.AddressDto;
import com.electromart.backend.dto.AddressRequest;

import java.util.List;

public interface AddressService {
    
    /**
     * Lấy danh sách địa chỉ của user
     */
    List<AddressDto> getUserAddresses(Long userId);
    
    /**
     * Lấy chi tiết một địa chỉ
     */
    AddressDto getAddressById(Long id, Long userId);
    
    /**
     * Lấy địa chỉ mặc định của user
     */
    AddressDto getDefaultAddress(Long userId);
    
    /**
     * Tạo địa chỉ mới
     */
    AddressDto createAddress(AddressRequest request);
    
    /**
     * Cập nhật địa chỉ
     */
    AddressDto updateAddress(Long id, AddressRequest request, Long userId);
    
    /**
     * Xóa địa chỉ
     */
    void deleteAddress(Long id, Long userId);
    
    /**
     * Đặt địa chỉ làm mặc định
     */
    AddressDto setDefaultAddress(Long id, Long userId);
}