package com.izzydrive.backend.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentPayingDTO {
    private boolean usingExistingPayingInfo;
    private KeyPairDTO onceTimeKeyPair;
}
