package com.example.healthgenie.boundedContext.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageRequest {

    private Long roomId;
    private String senderEmail;
    private String content;
}
