package com.izzydrive.backend.service.payment.data;

import com.izzydrive.backend.dto.payment.KeyPairDTO;
import com.izzydrive.backend.dto.payment.PaymentStatusDTO;

public interface PaymentDataService {

    PaymentStatusDTO getPaymentStatusForLoggedPassenger();

    boolean isValidETHAddress(String address);

    boolean isValidSecretKey(String key);

    boolean addressMatchSecretKey(KeyPairDTO keyPairDTO);

    KeyPairDTO getPaymentDataForCurrentLoggedUser();

    void savePaymentDataForCurrentLoggedUser(KeyPairDTO paymentData);

    void validatePaymentKeyPair(KeyPairDTO paymentData);
}
