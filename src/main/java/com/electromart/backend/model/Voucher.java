// PhieuGiamGia.java
package com.electromart.backend.model;

import com.electromart.backend.model.base.AuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "phieu_giam_gia")
public class Voucher extends AuditEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(precision = 16, scale = 2)
    private BigDecimal giamToiDa;

    private Integer phanTram;

    private Instant hieuLucTu;
    private Instant hieuLucDen;

    private Integer soLuongPhatHanh;
    private Integer soLuongDaDung = 0;
    private boolean hoatDong = true;

    public boolean isHoatDong() {
        return hoatDong;
    }

    public void setHoatDong(boolean hoatDong) {
        this.hoatDong = hoatDong;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getGiamToiDa() {
        return giamToiDa;
    }

    public void setGiamToiDa(BigDecimal giamToiDa) {
        this.giamToiDa = giamToiDa;
    }

    public Integer getPhanTram() {
        return phanTram;
    }

    public void setPhanTram(Integer phanTram) {
        this.phanTram = phanTram;
    }

    public Instant getHieuLucTu() {
        return hieuLucTu;
    }

    public void setHieuLucTu(Instant hieuLucTu) {
        this.hieuLucTu = hieuLucTu;
    }

    public Instant getHieuLucDen() {
        return hieuLucDen;
    }

    public void setHieuLucDen(Instant hieuLucDen) {
        this.hieuLucDen = hieuLucDen;
    }

    public Integer getSoLuongPhatHanh() {
        return soLuongPhatHanh;
    }

    public void setSoLuongPhatHanh(Integer soLuongPhatHanh) {
        this.soLuongPhatHanh = soLuongPhatHanh;
    }

    public Integer getSoLuongDaDung() {
        return soLuongDaDung;
    }

    public void setSoLuongDaDung(Integer soLuongDaDung) {
        this.soLuongDaDung = soLuongDaDung;
    }
}
