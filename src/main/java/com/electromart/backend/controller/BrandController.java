/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.electromart.backend.controller;

import com.electromart.backend.repository.BrandRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author duytu
 */
@RestController
@RequestMapping("/api/brands")
public class BrandController {
    private final BrandRepository repo;

    public BrandController(BrandRepository repo) {
        this.repo = repo;
    }
    
    public record BrandDto(Long id, String name) {}
    
    @GetMapping
    public List<BrandDto> all() {
        return repo.findAll().stream()
                .map(l -> new BrandDto(l.getId(), l.getTen()))
                .toList();
    }
}
