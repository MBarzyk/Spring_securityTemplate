package com.javagda25.securitytemplate.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountPasswordResetRequest {
    private Long accountId;
    private String resetpassword;
}
