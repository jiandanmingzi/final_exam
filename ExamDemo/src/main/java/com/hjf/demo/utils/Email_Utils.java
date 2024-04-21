package com.hjf.demo.utils;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Email_Utils {
    private static final String from = "tiaowangshijie@qq.com";
    private static final String password = "menrdlsqbbaadhcg";


    public static void sendEmail(String to, String title, String content) throws AddressException ,MessagingException{
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.qq.com");
        properties.put("mail.smtp.port", "587");
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        InternetAddress toAddress = new InternetAddress(to);
        message.setRecipient(Message.RecipientType.TO, toAddress);
        message.setSubject(title);
        message.setContent(content, "text/html;charset=UTF-8");
        Transport.send(message);
    }
}
