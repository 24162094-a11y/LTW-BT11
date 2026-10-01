package com.bookstore.store.utils;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public final class MailUtil_24162094 {
    private MailUtil_24162094() {
    }

    public static void sendOTP(String toEmail, String otp) {
        String host = setting("MAIL_HOST", "smtp.gmail.com");
        String port = setting("MAIL_PORT", "587");
        String username = setting("MAIL_USERNAME", "");
        String password = setting("MAIL_PASSWORD", "");

        if (username.isBlank() || password.isBlank()) {
            throw new IllegalStateException("MAIL_USERNAME and MAIL_PASSWORD must be configured");
        }

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Mã OTP kích hoạt tài khoản BookStore");
            message.setText("Mã OTP của bạn là: " + otp + "\nMã có hiệu lực trong 5 phút.");
            Transport.send(message);
        } catch (MessagingException exception) {
            throw new IllegalStateException("Could not send OTP email", exception);
        }
    }

    private static String setting(String name, String defaultValue) {
        String systemValue = System.getProperty(name);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }
        return System.getenv().getOrDefault(name, defaultValue);
    }
}
