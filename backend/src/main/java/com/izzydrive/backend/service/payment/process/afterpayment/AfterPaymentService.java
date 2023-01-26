package com.izzydrive.backend.service.payment.process.afterpayment;

import com.izzydrive.backend.model.Driving;

public interface AfterPaymentService {

    void onSuccess(Driving driving);

    void onFailure(Driving driving);
}
