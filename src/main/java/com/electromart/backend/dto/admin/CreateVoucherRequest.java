package com.electromart.backend.dto.admin;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class CreateVoucherRequest {

    @NotBlank
    @Size(max = 50)
    public String code;

    // % giảm (1-100) hoặc null
    @Min(1) @Max(100)
    public Integer phanTram;

    // giảm tối đa hoặc null
    @DecimalMin(value = "0.0", inclusive = true)
    public BigDecimal giamToiDa;

    public Instant hieuLucTu;
    public Instant hieuLucDen;

    // số lượng phát hành hoặc null
    @Min(1)
    public Integer soLuongPhatHanh;

    public Boolean hoatDong = true;
}
