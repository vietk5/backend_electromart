package com.electromart.backend.mapper;
import com.electromart.backend.dto.AddressDto;
import com.electromart.backend.dto.AddressRequest;
import com.electromart.backend.model.Address;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class AddressMapper {
    
    private static final DateTimeFormatter DATE_TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Chuyển từ Entity sang DTO
     */
    public AddressDto toDto(Address entity) {
        if (entity == null) {
            return null;
        }
        
        AddressDto dto = new AddressDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setTenNguoiNhan(entity.getTenNguoiNhan());
        dto.setSoDienThoai(entity.getSoDienThoai());
        dto.setTinhThanhPho(entity.getTinhThanhPho());
        dto.setQuanHuyen(entity.getQuanHuyen());
        dto.setPhuongXa(entity.getPhuongXa());
        dto.setDiaChiChiTiet(entity.getDiaChiChiTiet());
        dto.setLoaiDiaChi(entity.getLoaiDiaChi());
        dto.setIsDefault(entity.getIsDefault());
        
        if (entity.getCreatedAt() != null) {
            dto.setCreatedAt(entity.getCreatedAt().format(DATE_TIME_FORMATTER));
        }
        if (entity.getUpdatedAt() != null) {
            dto.setUpdatedAt(entity.getUpdatedAt().format(DATE_TIME_FORMATTER));
        }
        
        return dto;
    }
    
    /**
     * Chuyển từ Request sang Entity (cho tạo mới)
     */
    public Address toEntity(AddressRequest request) {
        if (request == null) {
            return null;
        }
        
        Address entity = new Address();
        entity.setUserId(request.getUserId());
        entity.setTenNguoiNhan(request.getTenNguoiNhan());
        entity.setSoDienThoai(request.getSoDienThoai());
        entity.setTinhThanhPho(request.getTinhThanhPho());
        entity.setQuanHuyen(request.getQuanHuyen());
        entity.setPhuongXa(request.getPhuongXa());
        entity.setDiaChiChiTiet(request.getDiaChiChiTiet());
        entity.setLoaiDiaChi(request.getLoaiDiaChi() != null ? request.getLoaiDiaChi() : "Nhà");
        entity.setIsDefault(request.getIsDefault() != null ? request.getIsDefault() : false);
        
        return entity;
    }
    
    /**
     * Cập nhật Entity từ Request (cho update)
     */
    public void updateEntity(Address entity, AddressRequest request) {
        if (entity == null || request == null) {
            return;
        }
        
        entity.setTenNguoiNhan(request.getTenNguoiNhan());
        entity.setSoDienThoai(request.getSoDienThoai());
        entity.setTinhThanhPho(request.getTinhThanhPho());
        entity.setQuanHuyen(request.getQuanHuyen());
        entity.setPhuongXa(request.getPhuongXa());
        entity.setDiaChiChiTiet(request.getDiaChiChiTiet());
        entity.setLoaiDiaChi(request.getLoaiDiaChi() != null ? request.getLoaiDiaChi() : "Nhà");
        
        if (request.getIsDefault() != null) {
            entity.setIsDefault(request.getIsDefault());
        }
    }
}
