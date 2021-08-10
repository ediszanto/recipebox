package com.example.receipebox.email;

import javax.mail.MessagingException;

public interface EmailSender {

    void sendEmail(String to, String from, String bodyText, Integer quantity) throws MessagingException;
}
