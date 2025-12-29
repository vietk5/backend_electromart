package com.electromart.backend.controller;

import com.electromart.backend.dto.*;
import com.electromart.backend.dto.admin.ApplyVoucherRequest;
import com.electromart.backend.dto.admin.ApplyVoucherResponse;
import com.electromart.backend.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vouchers")
public class VoucherController {

    private final VoucherService service;

    @PostMapping("/apply")
    public ApplyVoucherResponse apply(@RequestBody ApplyVoucherRequest req) {
        return service.apply(req);
    }
}
