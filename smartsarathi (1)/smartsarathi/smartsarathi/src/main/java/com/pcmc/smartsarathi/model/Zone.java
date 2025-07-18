package com.pcmc.smartsarathi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String code;

    private Boolean status; // 1 = ENABLED, 0 = DISABLED

    @ManyToOne
    @JsonIgnore
    private User createdUser;

    @ManyToOne
    @JsonIgnore
    private User modifiedUser;

    private LocalDate addedTime;
    private LocalDate modifiedTime;


}
