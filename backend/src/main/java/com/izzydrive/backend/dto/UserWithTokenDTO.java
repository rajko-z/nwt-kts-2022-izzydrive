package com.izzydrive.backend.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserWithTokenDTO {
    private UserDTO user;
    private String token;
}
