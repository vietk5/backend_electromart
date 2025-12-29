package com.electromart.backend.controller;


import com.electromart.backend.dto.admin.CreateVoucherRequest;
import com.electromart.backend.dto.admin.VoucherDto;
import com.electromart.backend.service.VoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/vouchers")
public class AdminVoucherController {

    private final VoucherService service;

    @GetMapping
    public List<VoucherDto> list() { return service.listAll(); }

    @PostMapping
    public VoucherDto create(@Valid @RequestBody CreateVoucherRequest req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public VoucherDto update(@PathVariable Long id, @Valid @RequestBody CreateVoucherRequest req) {
        return service.update(id, req);
    }

    @PatchMapping("/{id}/toggle")
    public VoucherDto toggle(@PathVariable Long id, @RequestParam boolean hoatDong) {
        return service.toggle(id, hoatDong);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}

