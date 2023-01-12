package com.izzydrive.backend.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MessageDTO {
    String sender;
    String text;
    String channel;

}
