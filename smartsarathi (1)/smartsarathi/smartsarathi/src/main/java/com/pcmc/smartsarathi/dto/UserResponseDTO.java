package com.pcmc.smartsarathi.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserResponseDTO {
    private long id;
    private String username;
    private String name;
    private String roles;
    private LocalDate addedTime;
    private LocalDate modifiedTime;
    private boolean status;


    @JsonIgnoreProperties(value = {"createdUser", "modifiedUser"}) // Ignore to prevent recursion
    private UserResponseDTO createdUser;

    @JsonIgnoreProperties(value = {"createdUser", "modifiedUser"}) // Ignore to prevent recursion
    private UserResponseDTO modifiedUser;


}
