package com.pcmc.smartsarathi.dto;


import lombok.Data;

import java.time.LocalDate;

@Data
public class PrivateStpResponseDTO {
    private long id;
    private String locationName;
    private double flow;
    private double doLevel;
    private double bod;
    private double cod;
    private double tss;
    private double ph;
    private String noOfTenaments;
    private String installedCapacity;
    private LocalDate addedTime;
    private String title; // Only the zone name in the response
    private boolean stpWorking;
    private boolean stpOms;

}
