package guestbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import javax.mail.internet.*;
import org.springframework.mail.javamail.MimeMessageHelper;
import javax.mail.MessagingException;

@Component
public class MailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    public void send(String to, String subject, String body) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;

        helper = new MimeMessageHelper(message, true);
        helper.setSubject(subject);
        helper.setTo(to);
        helper.setText(body, true);

        javaMailSender.send(message);
    }

    // SimpleMailMessage message = new SimpleMailMessage();
    // message.setTo(to);
    // message.setSubject(subject);
    // message.setText(text);
    // emailSender.send(message);
}
