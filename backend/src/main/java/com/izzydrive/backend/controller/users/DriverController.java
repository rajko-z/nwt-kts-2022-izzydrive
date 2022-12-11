package com.izzydrive.backend.controller.users;

import com.izzydrive.backend.dto.NewDriverDTO;
import com.izzydrive.backend.exception.CustomExceptionCode;
import com.izzydrive.backend.service.users.DriverService;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import java.io.IOException;

@Controller
@RequestMapping("/drivers")
@AllArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PostMapping("add")
    public ResponseEntity<Object> addNewDriver(@RequestBody NewDriverDTO driverDTO){
        try{
            driverService.addNewDriver(driverDTO);
            return ResponseEntity.ok("");
        }
        catch (MessagingException | TemplateException | IOException e) {
            return new ResponseEntity<>(CustomExceptionCode.getErrorMessageFromException(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
