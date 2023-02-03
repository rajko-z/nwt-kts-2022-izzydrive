package com.izzydrive.backend.service.payment.process;

import com.izzydrive.backend.dto.payment.CurrentPayingDTO;

public interface PaymentProcessService {

    boolean approvePayment(CurrentPayingDTO currentPayingData);
}
