package com.example.commoncallapi.exception;

import com.example.commoncallapi.dto.CallApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallApiException extends RuntimeException {
    private CallApiModel content;
    private String responseCode;
    private String responseMessage;
}
