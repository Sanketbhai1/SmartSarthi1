package com.pcmc.smartsarathi.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class STPForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "stp_form_id"))
    @Column(name = "uses")
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

    @Lob
    private byte[] societyPhoto; // You can store files as bytes

    @Lob
    private byte[] stpPhoto;

    @Lob
    private byte[] municipalOfficerPhoto;

    @ManyToOne
    @JsonIgnore
    private User createdUser;

    @ManyToOne
    @JsonIgnore
    private User modifiedUser;

    private LocalDate modifiedDate;

    private boolean status;


}

