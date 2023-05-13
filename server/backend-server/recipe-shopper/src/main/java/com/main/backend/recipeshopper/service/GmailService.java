package com.main.backend.recipeshopper.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.nimbusds.jose.util.Base64URL;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GmailService {
    @Autowired
    private GoogleCredentials credentials;

    public void sendEmail() {

        // Gmail API client
        Gmail service = new Gmail.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName("recipe-shopper")
                .build();

        log.debug(">>> Gmail client created...");

        try {
            Session session = Session.getDefaultInstance(new Properties());
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("pelie.888888@recipee-shopping.com"));
            message.addRecipient(RecipientType.TO, new InternetAddress("pelie.888888@gmail.com"));
            message.setSubject("test mail");
            message.setText("testing testing");

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            message.writeTo(buffer);

            Message email = new Message();
            email.setRaw(Base64URL.encode(buffer.toByteArray()).toString());

            Message response = service.users().messages().send(
                    "pelie.888888@recipee-shopping.com", email).execute();

            log.info(">>> Response: {}", response.toPrettyString());

        } catch (IOException e) {
            log.error("--- Error sending email: {}", e.getMessage());
        } catch (AddressException e) {
            log.error(e.getMessage());
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }
    }
}
