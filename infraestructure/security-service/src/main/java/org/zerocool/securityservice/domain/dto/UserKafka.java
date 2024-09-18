package org.zerocool.securityservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserKafka {
    private Long id;
    private String username;
    private String email;
}
