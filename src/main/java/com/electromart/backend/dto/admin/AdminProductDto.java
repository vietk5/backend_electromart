package com.electromart.backend.dto.admin;

import java.math.BigDecimal;

public class AdminProductDto {
    private Long id;
    private String ten;
    private BigDecimal gia;
    private Integer tonKho;
    private String imageUrl;
    private String moTaNgan;

    // Các trường dùng để gửi ID lên khi tạo mới/sửa
    private Long loaiId;
    private Long thuongHieuId;

    // Các trường dùng để hiển thị tên hoặc hứng tên mới nhập
    private String loaiTen;
    private String thuongHieuTen;

    // 1. Constructor mặc định (Bắt buộc cho Spring Boot)
    public AdminProductDto() {}

    // 2. Constructor đầy đủ (Dùng cho ProductMapper - ĐÂY LÀ CÁI BẠN ĐANG THIẾU)
    public AdminProductDto(Long id, String ten, BigDecimal gia, Integer tonKho,
                           String loaiTen, String thuongHieuTen, String imageUrl, String moTaNgan) {
        this.id = id;
        this.ten = ten;
        this.gia = gia;
        this.tonKho = tonKho;
        this.loaiTen = loaiTen;
        this.thuongHieuTen = thuongHieuTen;
        this.imageUrl = imageUrl;
        this.moTaNgan = moTaNgan;
    }

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }

    public BigDecimal getGia() { return gia; }
    public void setGia(BigDecimal gia) { this.gia = gia; }

    public Integer getTonKho() { return tonKho; }
    public void setTonKho(Integer tonKho) { this.tonKho = tonKho; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getMoTaNgan() { return moTaNgan; }
    public void setMoTaNgan(String moTaNgan) { this.moTaNgan = moTaNgan; }

    public Long getLoaiId() { return loaiId; }
    public void setLoaiId(Long loaiId) { this.loaiId = loaiId; }

    public Long getThuongHieuId() { return thuongHieuId; }
    public void setThuongHieuId(Long thuongHieuId) { this.thuongHieuId = thuongHieuId; }

    public String getLoaiTen() { return loaiTen; }
    public void setLoaiTen(String loaiTen) { this.loaiTen = loaiTen; }

    public String getThuongHieuTen() { return thuongHieuTen; }
    public void setThuongHieuTen(String thuongHieuTen) { this.thuongHieuTen = thuongHieuTen; }
}