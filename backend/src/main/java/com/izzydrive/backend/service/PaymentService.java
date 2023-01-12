package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.PaymentStatusDTO;

public interface PaymentService {

    PaymentStatusDTO getPaymentStatusForLoggedPassenger();
}
