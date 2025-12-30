package com.electromart.backend.controller;

import com.electromart.backend.dto.SpinRequest;
import com.electromart.backend.dto.SpinResultResponse;
import com.electromart.backend.dto.SpinStatusResponse;
import com.electromart.backend.service.SpinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/spin")
@RequiredArgsConstructor
public class SpinController {

    private final SpinService spinService;

    @GetMapping("/status")
    public SpinStatusResponse status(@RequestParam Long userId) {
        return spinService.status(userId);
    }

    @PostMapping
    public SpinResultResponse spin(@RequestBody SpinRequest req) {
        return spinService.spin(req.getUserId());
    }
}
