INSERT INTO loai_san_pham(id, ten, created_at, updated_at) VALUES
                                                               (1, 'CPU', now(), now()), (2, 'RAM', now(), now());

INSERT INTO thuong_hieu(id, ten, created_at, updated_at) VALUES
                                                             (1, 'Intel', now(), now()), (2, 'Kingston', now(), now());

-- Chèn sản phẩm (chú ý cột/ tên bảng khớp Hibernate output thực tế)
INSERT INTO san_pham(id, ten, loai_id, thuong_hieu_id, gia, mo_ta_ngan, image_url, created_at, updated_at) VALUES
                                                                                                               (1, 'CPU Intel Core i5-12400F', 1, 1, 3800000.00, '6 nhân 12 luồng', 'https://.../i5-12400f.jpg', now(), now()),
                                                                                                               (2, 'RAM Kingston Fury 16GB 3200', 2, 2, 790000.00, 'DDR4 3200', 'https://.../fury-16gb.jpg', now(), now());
