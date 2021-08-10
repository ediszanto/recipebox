package com.example.receipebox.service;


import com.example.receipebox.email.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements EmailSender {

    private final JavaMailSender javaMailSender;

    public void sendEmail(String to, String from, String ingredientName, Integer quantity) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            helper.setFrom(from);
            helper.setTo(to);
            helper.setText("Quantity of ingredient - " + ingredientName + " - got below 5! \n Current quantity: " + quantity);

            javaMailSender.send(mimeMessage);

            log.info("Email send succesfully");
        } catch (MessagingException exception){
            log.info(exception.getMessage());
            throw new RuntimeException("Mail failed to send!");
        }

    }



}
