// dto/VoucherDto.java
package com.electromart.backend.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
public class VoucherDto {
    public Long id;
    public String code;
    public Integer phanTram;
    public BigDecimal giamToiDa;
    public Instant hieuLucTu;
    public Instant hieuLucDen;
    public Integer soLuongPhatHanh;
    public Integer daSuDung;
    public boolean hoatDong;
}