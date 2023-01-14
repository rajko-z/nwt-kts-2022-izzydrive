package com.izzydrive.backend.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyPairDTO {
    @NotBlank
    private String secretKey;
    @NotBlank
    private String ethAddress;
}
