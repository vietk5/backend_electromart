// service/ProductService.java
package com.electromart.backend.service;

import com.electromart.backend.model.SanPham;
import com.electromart.backend.repository.SanPhamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service @RequiredArgsConstructor
public class ProductService {
    private final SanPhamRepository repo;
    public List<SanPham> findAll() { return repo.findAll(); }
    public SanPham get(Long id){ return repo.findById(id).orElseThrow(); }
}
