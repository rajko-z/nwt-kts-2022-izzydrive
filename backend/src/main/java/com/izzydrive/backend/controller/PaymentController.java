package com.izzydrive.backend.controller;

import com.izzydrive.backend.dto.TextResponse;
import com.izzydrive.backend.dto.payment.CurrentPayingDTO;
import com.izzydrive.backend.dto.payment.KeyPairDTO;
import com.izzydrive.backend.dto.payment.PaymentStatusDTO;
import com.izzydrive.backend.service.payment.data.PaymentDataService;
import com.izzydrive.backend.service.payment.process.PaymentProcessService;
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

    private final PaymentProcessService paymentProcessService;

    private final PaymentDataService paymentDataService;

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @GetMapping(value = "/status")
    public ResponseEntity<PaymentStatusDTO> getPaymentStatus() {
        PaymentStatusDTO paymentStatus = paymentDataService.getPaymentStatusForLoggedPassenger();
        return new ResponseEntity<>(paymentStatus, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @PutMapping(value="/payment-data")
    public ResponseEntity<TextResponse> savePaymentData(@RequestBody @Valid KeyPairDTO paymentData) {
        paymentDataService.savePaymentDataForCurrentLoggedUser(paymentData);
        return new ResponseEntity<>(new TextResponse("Success saving of payment data"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @GetMapping("/payment-data")
    public ResponseEntity<KeyPairDTO> getCurrentPaymentData() {
        KeyPairDTO keyPairDTO = paymentDataService.getPaymentDataForCurrentLoggedUser();
        return new ResponseEntity<>(keyPairDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @PutMapping("/pay")
    public ResponseEntity<TextResponse> pay(@RequestBody @Valid CurrentPayingDTO currentPaying) {
        paymentProcessService.approvePayment(currentPaying);
        return new ResponseEntity<>(new TextResponse("Success"), HttpStatus.OK);
    }
}
