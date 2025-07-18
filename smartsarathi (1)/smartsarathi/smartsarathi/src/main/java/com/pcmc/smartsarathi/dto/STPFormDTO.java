package com.pcmc.smartsarathi.dto;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
public class STPFormDTO {

    private long id;
    private LocalDate date;
    private String wardOffice;
    private String wardNo;
    private String societyNo;
    private String societyName;
    private String societyAddress;
    private String managerName;
    private String managerMobile;
    private String chairmanName;
    private String chairmanMobile;
    private String latitude;
    private String longitude;
    private String stpCapacity;
    private String technology;
    private String totalTenements;
    private String occupiedTenements;
    private String waterConnectionDiameter;
    private Boolean isWaterReused;
    private Boolean hasSeparateElectricity;
    private Boolean isMSEBBeneficiary;
    private Boolean hasOnlineMonitoring;
    private String apiID;
    private Boolean isStpOperational;
    private String nonOperationalDays;
    private String electricityUnits;
    private List<String> treatedWaterUses;
    private String remainingWaterDischarge;
    private Boolean hasNearbyStormWaterLine;
    private Boolean hasAppointedAgency;
    private String agencyName;
    private String otherReasons;
    private Boolean hasRWHSystem;
    private String rwhPitsCount;
    private Boolean isOWCOperational;
    private String stpOperationalIssues;
    private MultipartFile societyPhoto;
    private MultipartFile stpPhoto;
    private MultipartFile municipalOfficerPhoto;


}