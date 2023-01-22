package com.izzydrive.backend.service.payment.process.transfer;

import com.izzydrive.backend.model.Driving;

public interface PaymentTransferService {
    boolean payForAllPassengers(Driving driving);
}
