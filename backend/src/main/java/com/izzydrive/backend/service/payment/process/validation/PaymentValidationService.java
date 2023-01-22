package com.izzydrive.backend.service.payment.process.validation;

import com.izzydrive.backend.dto.payment.CurrentPayingDTO;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.users.Passenger;

public interface PaymentValidationService {
    boolean validateForPaymentSessionExpiration(Driving driving);

    boolean validateForPassengerPaymentApproval(Passenger passenger, CurrentPayingDTO currentPayingData);
}
