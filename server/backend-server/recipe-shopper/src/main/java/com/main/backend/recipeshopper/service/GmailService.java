package com.main.backend.recipeshopper.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.main.backend.recipeshopper.model.Cart;
import com.nimbusds.jose.util.Base64URL;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GmailService {
    private static final String TEMPLATE_CART_SUMMARY = "cart-summary.html";

    @Autowired
    private GoogleCredentials credentials;

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * Sends out an email to the receipient
     * 
     * @param cart
     * @param username
     * @param toEmail
     */
    public void sendEmail(Cart cart, String username, String toEmail) {
        try {
            // Gmail API client
            Gmail service = new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(credentials))
                    .setApplicationName("recipe-shopper")
                    .build();

            log.debug(">>> Gmail client created...");

            Session session = Session.getDefaultInstance(new Properties());
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("recipee-cart@recipee-shopping.com"));
            message.addRecipient(RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Cart Summary: %s".formatted(username));
            message.setContent(generateMail(cart, username), "text/html; charset=UTF-8");

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            message.writeTo(buffer);

            Message email = new Message();
            email.setRaw(Base64URL.encode(buffer.toByteArray()).toString());

            Message response = service.users().messages().send(
                    "me", email).execute();

            log.info(">>> Response: {}", response.toPrettyString());

        } catch (IOException e) {
            log.error("--- Error sending email: {}", e.getMessage());
        } catch (AddressException e) {
            log.error(e.getMessage());
        } catch (MessagingException e) {
            log.error(e.getMessage());
        } catch (GeneralSecurityException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Uses Thymeleaf to generate the mail template for the email body
     * 
     * @param cart
     * @param username
     * @return
     */
    private String generateMail(Cart cart, String username) {
        Context ctx = new Context();
        ctx.setVariable("cart", cart);
        ctx.setVariable("username", username);

        return templateEngine.process(TEMPLATE_CART_SUMMARY, ctx);
    }
}
