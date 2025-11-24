package com.electromart.backend.service;

import com.electromart.backend.model.KhachHang;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
@Service
@RequiredArgsConstructor
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    
    @Async
    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Mã OTP xác thực ElectroMart");
        message.setText("Mã OTP của bạn là: " + otp + "\nOTP có hiệu lực trong 5 phút.");
        mailSender.send(message);
    }
    
    // Gửi email xác nhận đăng ký
    @Async
    public void sendRegisterEmail(KhachHang kh) {
        try {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

        helper.setTo(kh.getEmail());
        helper.setSubject("Đăng ký thành công ứng dụng ElectroMart!");
        
        String content =
                "Xin chào " + kh.getHoTen() + ",\n\n" +
                "Chúc mừng bạn đã đăng ký thành công ứng dụng ElectroMart!\n" +
                "Email của bạn: " + kh.getEmail() + "\n\n" +
                "Chúc bạn mua sắm vui vẻ! Xin cảm ơn!";

        helper.setText(content, false);  // false = gửi text thường, true = HTML

        mailSender.send(mimeMessage);
    } catch (Exception e) {
        e.printStackTrace();
        }
    }
}
