package com.pcmc.smartsarathi.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private Long id;
    private String roles;
    private String token;

}
