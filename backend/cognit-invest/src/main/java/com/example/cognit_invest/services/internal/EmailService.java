package com.example.cognit_invest.services.internal;


import com.example.cognit_invest.models.auth.Token;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendConfirmationEmail(String destino, Token token) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");

        String link = "http://localhost:8080/auth/confirm?token="+ token.getToken();

        Context context = new Context();
        context.setVariable("confirmationLink",link);
        String html = templateEngine.process("email",context);

        helper.setFrom("suporte@horsecontrol.com.br");
        helper.setTo(destino);
        helper.setSubject("Confirmação de e-mail");
        helper.setText(html,true);
        mailSender.send(message);

        System.out.println("Email enviado para: "+destino);
    }

    public void sendRecoverEmail(String email,String code)throws MessagingException{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");

        Context context = new Context();
        context.setVariable("code",code);
        String html = templateEngine.process("recover",context);

        helper.setFrom("suporte@horsecontrol.com.br");
        helper.setTo(email);
        helper.setSubject("Recuperação de conta");
        helper.setText(html,true);
        mailSender.send(message);

        System.out.println("Email enviado para: "+email);
    }
}
