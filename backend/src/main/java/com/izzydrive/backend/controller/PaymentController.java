package com.izzydrive.backend.controller;

import com.izzydrive.backend.dto.PaymentStatusDTO;
import com.izzydrive.backend.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payment")
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @GetMapping(value = "/status")
    public ResponseEntity<PaymentStatusDTO> getPaymentStatus() {
        PaymentStatusDTO paymentStatus = paymentService.getPaymentStatusForLoggedPassenger();
        return new ResponseEntity<>(paymentStatus, HttpStatus.OK);
    }
}
