package com.izzydrive.backend.service.users;

import com.izzydrive.backend.dto.NewDriverDTO;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;

public interface DriverService {

    public void addNewDriver(NewDriverDTO driverDTO) throws TemplateException, MessagingException, IOException;
}
