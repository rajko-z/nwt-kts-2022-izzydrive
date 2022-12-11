package com.izzydrive.backend.email;

import com.izzydrive.backend.model.users.MyUser;
import freemarker.template.TemplateException;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;

import javax.mail.MessagingException;
import java.io.IOException;


public interface EmailSender {

    public void sendConfirmationAsync(String email, String token, String firstName) throws IOException, MessagingException, TemplateException;

    public void sendDriverRegistrationMail(String email, String password, String firstName) throws IOException, TemplateException, MessagingException;
}
