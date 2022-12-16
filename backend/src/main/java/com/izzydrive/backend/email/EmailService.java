package com.izzydrive.backend.email;

import com.izzydrive.backend.exception.InternalServerException;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender {

    @Value("${base-server-url}")
    private String baseServerUrl;

    private final JavaMailSender javaMailSender;

    private final Configuration freemarkerConfig;

    @Async
    public void sendConfirmationAsync(String email, String token, String firstName){
        try {
            String link = baseServerUrl + "/confirmation?token=" + token;
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message);

            // Using a subfolder such as /templates here
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates");

            Template t = freemarkerConfig.getTemplate("email-template.ftl");

            HashMap<String, Object> model = new HashMap<>();
            model.put("token", link);
            model.put("firstName", firstName);
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            helper.setTo(email);
            helper.setText(text, true);
            helper.setSubject("Confirm registration");
            javaMailSender.send(message);

        } catch (MessagingException | MailException |  TemplateException e) {
            throw new InternalServerException(ExceptionMessageConstants.MAIL_ERROR_MESSAGE);
        } catch (IOException e) {
            throw new InternalServerException(ExceptionMessageConstants.SOMETHING_WENT_WRONG_MESSAGE);
        }
    }

    @Override
    @Async
    public void sendDriverRegistrationMail(String email, String password) {
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates");
            Template t = freemarkerConfig.getTemplate("driver-registration-email.ftl");

            HashMap<String, Object> model = new HashMap<>();
            model.put("password", password);
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            helper.setTo(email);
            helper.setText(text, true);
            helper.setSubject("New driver registration");
            javaMailSender.send(message);

        } catch (MessagingException | MailException |  TemplateException e) {
            throw new InternalServerException(ExceptionMessageConstants.MAIL_ERROR_MESSAGE);
        } catch (IOException e) {
            throw new InternalServerException(ExceptionMessageConstants.SOMETHING_WENT_WRONG_MESSAGE);
        }
    }
}
