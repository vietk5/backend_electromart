package com.electromart.backend.service;

import com.electromart.backend.dto.AddressDto;
import com.electromart.backend.dto.AddressRequest;
import com.electromart.backend.model.Address;
import com.electromart.backend.exception.ResourceNotFoundException;
import com.electromart.backend.exception.UnauthorizedException;
import com.electromart.backend.mapper.AddressMapper;
import com.electromart.backend.repository.AddressRepository;
import com.electromart.backend.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {
    
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    
    @Override
    public List<AddressDto> getUserAddresses(Long userId) {
        log.info("Getting addresses for user: {}", userId);
        
        List<Address> addresses = addressRepository.findByUserIdOrderByIsDefaultDesc(userId);
        
        return addresses.stream()
                .map(addressMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public AddressDto getAddressById(Long id, Long userId) {
        log.info("Getting address by id: {} for user: {}", id, userId);
        
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ với ID: " + id));
        
        // Kiểm tra địa chỉ có thuộc về user không
        if (!address.getUserId().equals(userId)) {
            throw new UnauthorizedException("Bạn không có quyền truy cập địa chỉ này");
        }
        
        return addressMapper.toDto(address);
    }
    
    @Override
    public AddressDto getDefaultAddress(Long userId) {
        log.info("Getting default address for user: {}", userId);
        
        Address address = addressRepository.findByUserIdAndIsDefaultTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ mặc định"));
        
        return addressMapper.toDto(address);
    }
    
    @Override
    @Transactional
    public AddressDto createAddress(AddressRequest request) {
        log.info("Creating new address for user: {}", request.getUserId());
        
        // Nếu địa chỉ mới được đặt làm mặc định
        // Bỏ đặt tất cả địa chỉ mặc định cũ của user
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressRepository.unsetDefaultForUser(request.getUserId());
        } else {
            // Nếu đây là địa chỉ đầu tiên của user, tự động đặt làm mặc định
            long addressCount = addressRepository.countByUserId(request.getUserId());
            if (addressCount == 0) {
                request.setIsDefault(true);
            }
        }
        
        Address address = addressMapper.toEntity(request);
        Address savedAddress = addressRepository.save(address);
        
        log.info("Address created successfully with id: {}", savedAddress.getId());
        return addressMapper.toDto(savedAddress);
    }
    
    @Override
    @Transactional
    public AddressDto updateAddress(Long id, AddressRequest request, Long userId) {
        log.info("Updating address id: {} for user: {}", id, userId);
        
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ với ID: " + id));
        
        // Kiểm tra quyền sở hữu
        if (!address.getUserId().equals(userId)) {
            throw new UnauthorizedException("Bạn không có quyền cập nhật địa chỉ này");
        }
        
        // Nếu địa chỉ được đặt làm mặc định
        if (Boolean.TRUE.equals(request.getIsDefault()) && !address.getIsDefault()) {
            addressRepository.unsetDefaultForUser(userId);
        }
        
        addressMapper.updateEntity(address, request);
        Address updatedAddress = addressRepository.save(address);
        
        log.info("Address updated successfully");
        return addressMapper.toDto(updatedAddress);
    }
    
    @Override
    @Transactional
    public void deleteAddress(Long id, Long userId) {
        log.info("Deleting address id: {} for user: {}", id, userId);
        
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ với ID: " + id));
        
        // Kiểm tra quyền sở hữu
        if (!address.getUserId().equals(userId)) {
            throw new UnauthorizedException("Bạn không có quyền xóa địa chỉ này");
        }
        
        boolean wasDefault = address.getIsDefault();
        addressRepository.delete(address);
        
        // Nếu địa chỉ vừa xóa là mặc định, đặt địa chỉ khác làm mặc định
        if (wasDefault) {
            List<Address> remainingAddresses = addressRepository.findByUserIdOrderByIsDefaultDesc(userId);
            if (!remainingAddresses.isEmpty()) {
                Address newDefault = remainingAddresses.get(0);
                newDefault.setIsDefault(true);
                addressRepository.save(newDefault);
            }
        }
        
        log.info("Address deleted successfully");
    }
    
    @Override
    @Transactional
    public AddressDto setDefaultAddress(Long id, Long userId) {
        log.info("Setting default address id: {} for user: {}", id, userId);
        
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ với ID: " + id));
        
        // Kiểm tra quyền sở hữu
        if (!address.getUserId().equals(userId)) {
            throw new UnauthorizedException("Bạn không có quyền đặt địa chỉ này làm mặc định");
        }
        
        // Bỏ đặt tất cả địa chỉ mặc định cũ
        addressRepository.unsetDefaultForUser(userId);
        
        // Đặt địa chỉ này làm mặc định
        address.setIsDefault(true);
        Address updatedAddress = addressRepository.save(address);
        
        log.info("Default address set successfully");
        return addressMapper.toDto(updatedAddress);
    }
}