package com.pcmc.smartsarathi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class PrivateStp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String locationName;
    private String installedCapacity;
    private String noOfTenaments;

    private boolean isWorking;
    private boolean isOms;


    @ManyToOne
    @JsonIgnore
    private Zone zone;

    @ManyToOne
    @JsonIgnore
    private User createdUser;

    @ManyToOne
    @JsonIgnore
    private User modifiedUser;

    private LocalDate addedTime;
    private LocalDate modifiedTime;

    // Realtime parameters
    private double flow;
    private double doLevel;
    private double bod;
    private double cod;
    private double tss;
    private double ph;
    private boolean status;
}

