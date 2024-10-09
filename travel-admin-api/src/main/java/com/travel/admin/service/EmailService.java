package com.travel.admin.service;


public interface EmailService {
    /**
     * Send verification code
     * @param toEmail Email address to send to
     * @param code Verification Code
     */
    void sendVerificationCode(String toEmail,String code);

    /**
     * Send notification email
     * @param toEmail Email address to send to
     * @param subject subject
     * @param text text
     */
    void sendSimpleEmail(String toEmail, String subject, String text);
}
