package com.izzydrive.backend.email;

import com.izzydrive.backend.exception.InternalServerException;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
public class EmailService implements EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment env;

    @Autowired
    private Configuration freemarkerConfig;

    @Async
    public void sendConfirmationAsync(String email, String token, String firstName){
//        SimpleMailMessage mail = new SimpleMailMessage();
//        mail.setTo(email);
//        mail.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
//        mail.setSubject("Primer slanja emaila pomoću Spring taska");
//        String link = "https://localhost:8443/izzydrive/v1/confirmation?token=" + token;
//        mail.setText("Hello, activate your account on this link: " + link + ".");

        try {

            String link = "https://localhost:8443/izzydrive/v1/confirmation?token=" + token;
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
}
