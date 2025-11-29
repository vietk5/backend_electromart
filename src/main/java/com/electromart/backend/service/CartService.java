package com.electromart.backend.service;

import com.electromart.backend.dto.CartAddRequest;
import com.electromart.backend.dto.CartItemDto;
import com.electromart.backend.dto.CheckoutRequest;
import com.electromart.backend.model.*;
import com.electromart.backend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final GioHangRepository gioHangRepository;
    private final KhachHangRepository khachHangRepository;
    private final SanPhamRepository sanPhamRepository;
    private final DonHangRepository donHangRepository;
    private  final GioHangItemRepository gioHangItemRepository;


    // ====== THÊM SẢN PHẨM VÀO GIỎ ======
    public void addToCart(CartAddRequest req) {
        Long userId = req.getUserId();
        Long productId = req.getProductId();
        int quantity = req.getQuantity();

        if (quantity <= 0) quantity = 1;

        KhachHang kh = khachHangRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Khách hàng không tồn tại"));

        // tìm hoặc tạo giỏ
        GioHang cart = gioHangRepository.findByKhachHangId(userId)
                .orElseGet(() -> {
                    GioHang g = new GioHang();
                    g.setKhachHang(kh);
                    return gioHangRepository.save(g);
                });

        SanPham sp = sanPhamRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Sản phẩm không tồn tại"));

        // tìm item cùng sản phẩm trong giỏ
        Optional<GioHangItem> optItem = cart.getItems().stream()
                .filter(i -> i.getSanPham().getId().equals(productId))
                .findFirst();

        GioHangItem item;
        if (optItem.isPresent()) {
            item = optItem.get();
            item.setSoLuong(item.getSoLuong() + quantity);
            // đảm bảo có khachHang (phòng trường hợp row cũ)
            if (item.getKhachHang() == null) {
                item.setKhachHang(kh);
            }
        } else {
            item = new GioHangItem();
            item.setGioHang(cart);
            item.setKhachHang(kh);        // ⭐⭐ DÒNG QUAN TRỌNG
            item.setSanPham(sp);
            item.setSoLuong(quantity);
            item.setDonGia(sp.getGia());
            cart.getItems().add(item);
        }

        gioHangRepository.save(cart);
    }

    // ====== LẤY GIỎ HÀNG ======
    public List<CartItemDto> getCart(Long userId) {
        GioHang cart = gioHangRepository.findByKhachHangId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Giỏ hàng không tồn tại"));

        return cart.getItems().stream()
                .map(i -> new CartItemDto(
                        i.getId(),
                        i.getSanPham().getId(),
                        i.getSanPham().getTen(),
                        i.getSanPham().getImageUrl(),
                        i.getDonGia(),
                        i.getSoLuong(),
                        i.getThanhTien()
                ))
                .collect(Collectors.toList());
    }

    // ====== CHECKOUT: TẠO ĐƠN HÀNG + XOÁ ITEM TRONG GIỎ ======
    public Long checkout(CheckoutRequest request) {
        Long userId = request.getUserId();
        List<Long> productIds = request.getProductIds();

        if (userId == null) {
            throw new IllegalArgumentException("userId không được null");
        }

        KhachHang kh = khachHangRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Khách hàng không tồn tại"));

        GioHang cart = gioHangRepository.findByKhachHangId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Giỏ hàng không tồn tại"));

        // Lấy danh sách item cần checkout
        List<GioHangItem> itemsToCheckout;
        if (productIds == null || productIds.isEmpty()) {
            // Nếu không truyền productIds -> checkout toàn bộ giỏ
            itemsToCheckout = new ArrayList<>(cart.getItems());
        } else {
            itemsToCheckout = cart.getItems().stream()
                    .filter(i -> productIds.contains(i.getSanPham().getId()))
                    .collect(Collectors.toList());
        }

        if (itemsToCheckout.isEmpty()) {
            // không có gì để tạo đơn
            return userId;
        }

        // ===== TẠO ĐƠN HÀNG =====
        DonHang order = new DonHang();
        order.setKhachHang(kh);
        order.setNgayDatHang(LocalDateTime.now());
        order.setTrangThai(TrangThaiDonHang.DANG_XU_LY); // hoặc trạng thái mặc định bạn muốn

        // parse phương thức thanh toán từ request (COD / ONLINE ...)
        try {
            order.setPhuongThuc(PhuongThucThanhToan.valueOf(
                    request.getPaymentMethod().toUpperCase()
            ));
        } catch (Exception ex) {
            // nếu sai thì cho default COD
            order.setPhuongThuc(PhuongThucThanhToan.COD);
        }

        // nếu DonHang có các field người nhận, địa chỉ... thì set thêm ở đây
        // order.setTenNguoiNhan(request.getReceiverName());
        // order.setSoDienThoaiNguoiNhan(request.getReceiverPhone());
        // order.setDiaChiNguoiNhan(request.getReceiverAddress());

        List<ChiTietDonHang> chiTiets = new ArrayList<>();
        for (GioHangItem i : itemsToCheckout) {
            ChiTietDonHang ct = new ChiTietDonHang();
            ct.setDonHang(order);
            ct.setSanPham(i.getSanPham());
            ct.setSoLuong(i.getSoLuong());
            ct.setDonGia(i.getDonGia());
            // nếu entity có field thanhTien thì set, còn nếu chỉ có getter tính toán thì bỏ
            // ct.setThanhTien(i.getThanhTien());
            chiTiets.add(ct);
        }
        order.setChiTiet(chiTiets);

        // Lưu đơn hàng (cascade sẽ tự lưu chi tiết)
        donHangRepository.save(order);

        // ===== XOÁ CÁC ITEM ĐÃ CHECKOUT KHỎI GIỎ =====
        cart.getItems().removeAll(itemsToCheckout);
        gioHangRepository.save(cart);
        return userId;
    }
    @Transactional
    public void removeItem(int userId, Long productId) {
        // Ví dụ nếu bạn có GioHangItemRepository
        GioHang cart = gioHangRepository.findByKhachHangId((long) userId)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));

        gioHangItemRepository.deleteByGioHangIdAndSanPhamId(cart.getId(), productId);
    }

}
