package com.curriculum.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@ConfigurationProperties(prefix = "spring.mail")
public class MailConfiguration {
//    private String host;
//    private String username;
//    private String password;
//    private String defaultEncoding;
//    private String port;
//
//    /*
//    *   mail:
//    host: smtp.qq.com
//    port: "465"
//    username: 369496090@qq.com
//    password: uueqxffsycwzbifc
//    default-encoding: utf-8*/
//    @Bean
//    public JavaMailSenderImpl JavaMailSender(){
//        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//        javaMailSender.setHost("smtp.qq.com");
//        javaMailSender.setUsername("369496090@qq.com");
//        javaMailSender.setPassword("porkexfxcjfnbiac");
//        return javaMailSender;
//    }
}
