package com.izzydrive.backend.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AdminResponseOnChanges {

    String driverEmail;
    String response;
}
