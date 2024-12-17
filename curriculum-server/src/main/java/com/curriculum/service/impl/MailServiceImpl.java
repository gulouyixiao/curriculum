package com.curriculum.service.impl;

import com.curriculum.service.MailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

//    @Autowired
//    private JavaMailSender mailSender;
//
//    @Override
//    public void sendSimpleMail(String to, String subject, String content) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        // 设置收件人
//        message.setTo(to);
//        // 设置主题
//        message.setSubject(subject);
//        // 设置邮件内容
//        message.setText(content);
//        // 设置发件人，从配置文件中读取
//        mailSender.send(message);
//    }
}

