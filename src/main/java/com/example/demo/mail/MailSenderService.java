package com.example.demo.mail;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
@Service
public class MailSenderService {
    private static final String DOMAIN_OWNER_EMAIL = "frodson35@gmail.com";

    private final JavaMailSender javaMailSender;

    public MailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMailsFromContactForm(String email, String username, String content) throws MessagingException {
        sendEmailToDomainOwner(email, username, content);
        sendConfirmationToClient(email, username, content);
    }

    private void sendEmailToDomainOwner(String email, String username, String content) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setTo(DOMAIN_OWNER_EMAIL);
        mimeMessageHelper.setFrom(email);
        mimeMessageHelper.setSubject("Wiadomość z formularza kontaktowego");
        mimeMessageHelper.setReplyTo(email);
        String text = "<p>Wiadomość od: " + username + "(" + email + ") </p>";
        text += "<p>Treść wiadomości: </p>";
        text += content;
        mimeMessageHelper.setText(text, true);

        javaMailSender.send(mimeMessage);
    }

    private void sendConfirmationToClient(String email, String username, String content) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setFrom(DOMAIN_OWNER_EMAIL);
        mimeMessageHelper.setSubject("Potwierdzenie wysłania wiadomości");
        mimeMessageHelper.setReplyTo(DOMAIN_OWNER_EMAIL);
        String text = "<p>Cześć " + username + "! Potwierdzamy wysłanie wiadomości. Za niedługo odpiszemy.</p>";
        text += "<p>Treść wiadomości: </p>";
        text += content;
        mimeMessageHelper.setText(text, true);

        javaMailSender.send(mimeMessage);
    }

    public void sendPasswordResetLink(String email, String key) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setFrom(DOMAIN_OWNER_EMAIL);
        mimeMessageHelper.setSubject("Link do resetu hasła");

        String link = "<a href=\"http://localhost:8080/resetHasla?klucz=" + key + "\">TUTAJ</a>";

        String text = "<p>Cześć! Tutaj link do resetu hasła: + " + link + "</p>";
        mimeMessageHelper.setText(text, true);

        javaMailSender.send(mimeMessage);
    }
}
