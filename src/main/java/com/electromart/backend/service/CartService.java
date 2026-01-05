package com.electromart.backend.service;

import com.electromart.backend.dto.CartAddRequest;
import com.electromart.backend.dto.CartItemDto;
import com.electromart.backend.dto.CheckoutItem;
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
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
    private  final VoucherService  voucherService;


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
        int isBuyNow = request.getIsBuyNow();
        //MUA HÀNG NGAY
        if (isBuyNow == 1) {
            Long userId = request.getUserId();
            List<CheckoutItem> product = request.getProduct();
            if (userId == null) {
                throw new IllegalArgumentException("userId không được null");
            }
            KhachHang kh = khachHangRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Khách hàng không tồn tại"));
            GioHang cart = gioHangRepository.findByKhachHangId(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Giỏ hàng không tồn tại"));
            DonHang order = new DonHang();
            order.setKhachHang(kh);
            order.setNgayDatHang(LocalDateTime.now());
            order.setTrangThai(TrangThaiDonHang.DANG_XU_LY);
            order.setTenNguoiNhan(request.getReceiverName());
            order.setSoDienThoaiNhan(request.getReceiverPhone());
            order.setDiaChiNhan(request.getReceiverAddress());

            if ("COD".equalsIgnoreCase(request.getPaymentMethod())) {
                order.setPhuongThuc(PhuongThucThanhToan.COD);
            } else {
                order.setPhuongThuc(PhuongThucThanhToan.VNPAY);
            }


            List<ChiTietDonHang> chiTiets = new ArrayList<>();

            for (CheckoutItem pd : product) {
                SanPham sp = sanPhamRepository.findById(pd.getProductId())
                        .orElseThrow(() -> new EntityNotFoundException("Sản phẩm ID " + pd.getProductId() + " không tồn tại"));

                ChiTietDonHang ct = new ChiTietDonHang();
                ct.setDonHang(order);
                ct.setSanPham(sp);
                ct.setSoLuong(pd.getQuantity()); 
                ct.setDonGia(sp.getGia());
                chiTiets.add(ct);
            }

            order.setChiTiet(chiTiets);
            donHangRepository.save(order);
            voucherService.consumeOnCheckout(request.getVoucherCode());
            return userId;
        }   
        
        Long userId = request.getUserId();
        List<CheckoutItem> product = request.getProduct();

        if (userId == null) {
            throw new IllegalArgumentException("userId không được null");
        }

        KhachHang kh = khachHangRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Khách hàng không tồn tại"));

        GioHang cart = gioHangRepository.findByKhachHangId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Giỏ hàng không tồn tại"));

        // Lấy danh sách item cần checkout
        List<GioHangItem> itemsToCheckout;
        if (product == null || product.isEmpty()) {
            // Nếu không truyền productIds -> checkout toàn bộ giỏ
            itemsToCheckout = new ArrayList<>(cart.getItems());
        } else {
            // Tạo một tập hợp các ID cần thanh toán để so sánh cho nhanh
            Set<Long> productIdsToBuy = product.stream()
                    .map(CheckoutItem::getProductId)
                    .collect(Collectors.toSet());

            itemsToCheckout = cart.getItems().stream()
                    .filter(i -> productIdsToBuy.contains(i.getSanPham().getId()))
                    .collect(Collectors.toList());
        }
        
        if (itemsToCheckout.isEmpty()) {
           return userId;
        }

        // ===== TẠO ĐƠN HÀNG =====
        DonHang order = new DonHang();
        order.setKhachHang(kh);
        order.setNgayDatHang(LocalDateTime.now());
        order.setTrangThai(TrangThaiDonHang.DANG_XU_LY);
        order.setTenNguoiNhan(request.getReceiverName());
        order.setSoDienThoaiNhan(request.getReceiverPhone());
        order.setDiaChiNhan(request.getReceiverAddress());

        
        if ("COD".equalsIgnoreCase(request.getPaymentMethod())) {
            order.setPhuongThuc(PhuongThucThanhToan.COD);
        } else {
            order.setPhuongThuc(PhuongThucThanhToan.VNPAY);
        }

        Map<Long, Integer> quantityMap = product.stream()
        .collect(Collectors.toMap(
                CheckoutItem::getProductId,
                CheckoutItem::getQuantity
        ));

        List<ChiTietDonHang> chiTiets = new ArrayList<>();
        for (GioHangItem i : itemsToCheckout) {
            ChiTietDonHang ct = new ChiTietDonHang();
            ct.setDonHang(order);
            ct.setSanPham(i.getSanPham());
            
//            ct.setSoLuong(i.getSoLuong());
            int qty = quantityMap.getOrDefault(
                        i.getSanPham().getId(),
                        i.getSoLuong()
                );

                ct.setSoLuong(qty);

            ct.setDonGia(i.getDonGia());
            // nếu entity có field thanhTien thì set, còn nếu chỉ có getter tính toán thì bỏ
            // ct.setThanhTien(i.getThanhTien());
            chiTiets.add(ct);
        }
        order.setChiTiet(chiTiets);

        // Lưu đơn hàng (cascade sẽ tự lưu chi tiết)
        donHangRepository.save(order);
        voucherService.consumeOnCheckout(request.getVoucherCode());

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
