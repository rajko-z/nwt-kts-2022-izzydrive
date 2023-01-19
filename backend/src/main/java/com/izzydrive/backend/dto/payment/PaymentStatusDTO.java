package com.izzydrive.backend.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentStatusDTO {
    private boolean passengerApproved;
    private boolean waitingForOthers;
    private boolean sessionExpired;
    private double priceInRSD;
    private double priceInETH;
}
