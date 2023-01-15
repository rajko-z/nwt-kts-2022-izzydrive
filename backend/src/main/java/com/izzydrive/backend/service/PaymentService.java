package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.payment.KeyPairDTO;
import com.izzydrive.backend.dto.payment.PaymentStatusDTO;

public interface PaymentService {

    PaymentStatusDTO getPaymentStatusForLoggedPassenger();

    boolean isValidETHAddress(String address);

    boolean isValidSecretKey(String key);

    boolean addressMatchSecretKey(KeyPairDTO keyPairDTO);

    KeyPairDTO getPaymentDataForCurrentLoggedUser();

    void savePaymentDataForCurrentLoggedUser(KeyPairDTO paymentData);
}
