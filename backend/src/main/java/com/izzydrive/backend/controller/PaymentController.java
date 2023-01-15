package com.izzydrive.backend.controller;

import com.izzydrive.backend.dto.TextResponse;
import com.izzydrive.backend.dto.payment.KeyPairDTO;
import com.izzydrive.backend.dto.payment.PaymentStatusDTO;
import com.izzydrive.backend.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

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

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @PutMapping(value="/payment-data")
    public ResponseEntity<TextResponse> savePaymentData(@RequestBody @Valid KeyPairDTO paymentData) {
        paymentService.savePaymentDataForCurrentLoggedUser(paymentData);
        return new ResponseEntity<>(new TextResponse("Success saving of payment data"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @GetMapping("/payment-data")
    public ResponseEntity<KeyPairDTO> getCurrentPaymentData() {
        KeyPairDTO keyPairDTO = paymentService.getPaymentDataForCurrentLoggedUser();
        return new ResponseEntity<>(keyPairDTO, HttpStatus.OK);
    }

}
