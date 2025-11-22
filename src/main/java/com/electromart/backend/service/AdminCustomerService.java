package com.electromart.backend.service;

import com.electromart.backend.dto.admin.AdminCustomerDto;
import com.electromart.backend.mapper.CustomerMapper;
import com.electromart.backend.model.KhachHang;
import com.electromart.backend.repository.KhachHangRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class AdminCustomerService {

    private final KhachHangRepository repo;

    public AdminCustomerService(KhachHangRepository repo) {
        this.repo = repo;
    }

    public Page<AdminCustomerDto> searchCustomers(String search,
                                                  int page,
                                                  int size,
                                                  boolean newestFirst) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                newestFirst ? Sort.by("createdAt").descending()
                        : Sort.by("createdAt").ascending()
        );

        Page<KhachHang> p;
        if (search == null || search.isBlank()) {
            p = repo.findAll(pageable);
        } else {
            p = repo.searchByKeyword(search.trim(), pageable);
        }

        return new PageImpl<>(
                p.getContent().stream()
                        .map(CustomerMapper::toAdminDto)
                        .collect(Collectors.toList()),
                pageable,
                p.getTotalElements()
        );
    }

    @Transactional
    public void toggleBlock(Long id) {
        KhachHang k = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        k.setBiKhoa(!k.getBiKhoa());
        repo.save(k);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        repo.deleteById(id);
    }
}
