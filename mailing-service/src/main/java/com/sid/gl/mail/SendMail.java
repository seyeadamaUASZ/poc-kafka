package com.sid.gl.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendMail {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to,String body,String topic) {
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setFrom("ediaymaecom@gmail.com");
        mailMessage.setTo(to);
        mailMessage.setSubject(topic);
        mailMessage.setText(body);
        javaMailSender.send(mailMessage);

        System.out.println("mail sending successfully!!");
    }

}
