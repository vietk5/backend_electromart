package com.electromart.backend.repository;

import com.electromart.backend.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
    // Lấy tất cả địa chỉ của user, sắp xếp địa chỉ mặc định lên đầu
    @Query("SELECT a FROM Address a WHERE a.userId = :userId ORDER BY a.isDefault DESC, a.createdAt DESC")
    List<Address> findByUserIdOrderByIsDefaultDesc(@Param("userId") Long userId);
    
    // Lấy địa chỉ mặc định của user
    Optional<Address> findByUserIdAndIsDefaultTrue(Long userId);
    
    // Đếm số địa chỉ của user
    long countByUserId(Long userId);
    
    // Kiểm tra xem địa chỉ có thuộc về user không
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Address a WHERE a.id = :id AND a.userId = :userId")
    boolean existsByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
    
    // Bỏ đặt tất cả địa chỉ mặc định của user
    @Modifying
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.userId = :userId")
    void unsetDefaultForUser(@Param("userId") Long userId);
    
    // Xóa tất cả địa chỉ của user
    void deleteByUserId(Long userId);
}
