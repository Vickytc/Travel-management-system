package com.travel.admin.service.impl;

import com.travel.admin.exception.BusinessException;
import com.travel.admin.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;


@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Resource
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendVerificationCode(String toEmail, String code) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("[TravelAdmin] User Verification");
            message.setText("The email verification code for this request is: " + code + "\n\nThis code is valid for 3 minutes, please enter it promptly. (Please do not disclose this code)\n"
                    + "\nIf this was not initiated by you, please ignore this email.\n(This is an automatically sent email, please do not reply directly)");
            javaMailSender.send(mimeMessage);
            log.info("Verification code sent successfully: [{}] => [{}]", code, toEmail);
        } catch (Exception e) {
            log.error("Failed to send verification code: [Registered User: {}]", toEmail, e);
            throw new BusinessException("Failed to send verification code, please check if the email input is correct");
        }
    }

    @Override
    public void sendSimpleEmail(String toEmail, String subject, String text) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(text);
            javaMailSender.send(mimeMessage);
            log.info("Email sent successfully: [{}] => [{}]", subject, toEmail);
        } catch (Exception e) {
            log.error("Failed to send email: [{}] -> [{}]", subject, toEmail, e);
            throw new BusinessException("Failed to send email, please check if the email input is correct");
        }
    }
}

