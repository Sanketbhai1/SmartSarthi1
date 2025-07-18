package com.pcmc.smartsarathi.dto;


import lombok.Data;

import java.time.LocalDate;

@Data
public class PrivateStpDTO {
    private long id;
    private String locationName;
    private String installedCapacity;
    private String noOfTenaments;
    private String flow;
    private String ph;
    private String doLevel;
    private String bod;
    private String cod;
    private String tss;
    private LocalDate addedTime;
    private String title;
    private Boolean stpWorking;
    private Boolean stpOms;

}
