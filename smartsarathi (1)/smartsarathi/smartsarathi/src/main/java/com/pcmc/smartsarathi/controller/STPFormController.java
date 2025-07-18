package com.pcmc.smartsarathi.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.pcmc.smartsarathi.dto.STPFormDTO;
import com.pcmc.smartsarathi.model.STPForm;
import com.pcmc.smartsarathi.model.User;
import com.pcmc.smartsarathi.service.STPFormService;
import com.pcmc.smartsarathi.service.UserService;
import com.pcmc.smartsarathi.utils.ExceptionMessages;
import com.pcmc.smartsarathi.utils.ExceptionUtils;
import jakarta.validation.Valid;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stpform")
public class STPFormController {

    @Autowired
    private UserService userService;

    @Autowired
    private STPFormService stpFormService;

    private static final Logger log = Logger.getLogger(STPFormController.class);

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<?> addSTPForm(@ModelAttribute STPFormDTO dto, @RequestParam("societyPhoto") MultipartFile societyPhoto,
                                                     @RequestParam("stpPhoto") MultipartFile stpPhoto, @RequestParam("municipalOfficerPhoto") MultipartFile municipalOfficerPhoto
    ) {
        User loginUser = null;
        try {
            loginUser = userService.getAuthenticateUser();
            if (loginUser == null) {
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }

            if (dto.getDate() == null) {
                return new ResponseEntity<>("Date is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getWardOffice() == null) {
                return new ResponseEntity<>("Word Office is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getWardNo() == null) {
                return new ResponseEntity<>("Word Number is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getSocietyNo() == null) {
                return new ResponseEntity<>("Society No is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getSocietyName() == null) {
                return new ResponseEntity<>("Society Name is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getSocietyAddress() == null) {
                return new ResponseEntity<>("Society Address is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getManagerName() == null) {
                return new ResponseEntity<>("Manager Name is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getManagerMobile() == null) {
                return new ResponseEntity<>(" Manager Mobile No is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getChairmanName() == null) {
                return new ResponseEntity<>("Chairman Name is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getChairmanMobile() == null) {
                return new ResponseEntity<>("Chairman Mobile No is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getLatitude() == null) {
                return new ResponseEntity<>("Latitude is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getLongitude() == null) {
                return new ResponseEntity<>("Longitude is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getStpCapacity() == null) {
                return new ResponseEntity<>("Stp Capacity is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getTechnology() == null) {
                return new ResponseEntity<>("Technology is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getTotalTenements() == null) {
                return new ResponseEntity<>("TotalTenements is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getOccupiedTenements() == null) {
                return new ResponseEntity<>("Occupied Tenements is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getWaterConnectionDiameter() == null) {
                return new ResponseEntity<>(" is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getIsWaterReused() == null) {
                return new ResponseEntity<>("Water Reused is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getHasSeparateElectricity() == null) {
                return new ResponseEntity<>("Separate Electricity is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getIsMSEBBeneficiary() == null) {
                return new ResponseEntity<>("MSEB Beneficiary is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getHasOnlineMonitoring() == null) {
                return new ResponseEntity<>("Online Monitoring is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getApiID() == null) {
                return new ResponseEntity<>("Api Id is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getIsStpOperational() == null) {
                return new ResponseEntity<>("Stp Operational is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getNonOperationalDays() == null) {
                return new ResponseEntity<>("NonOperational Days is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getElectricityUnits() == null) {
                return new ResponseEntity<>("Electricity Units is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getTreatedWaterUses() == null) {
                return new ResponseEntity<>("Treated Water Uses is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getRemainingWaterDischarge() == null) {
                return new ResponseEntity<>("Remaining Water Discharge is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getHasNearbyStormWaterLine() == null) {
                return new ResponseEntity<>("Near by Storm Water Line is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getHasAppointedAgency() == null) {
                return new ResponseEntity<>("Appointed Agency is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getAgencyName() == null) {
                return new ResponseEntity<>("Agency Name is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getOtherReasons() == null) {
                return new ResponseEntity<>("OtherReasons is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getHasRWHSystem() == null) {
                return new ResponseEntity<>("RWH System is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getRwhPitsCount() == null) {
                return new ResponseEntity<>("Rwh Pits Count is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getIsOWCOperational() == null) {
                return new ResponseEntity<>("OWC Operational is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getStpOperationalIssues() == null) {
                return new ResponseEntity<>("Stp Operational Issues is empty", HttpStatus.BAD_REQUEST);
            }
            if (societyPhoto != null && !societyPhoto.isEmpty()) {
                if (!isValidImageType(societyPhoto)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Map.of("error", "Invalid Society Photo format. Only JPG,SVG, JPEG, or PNG files are allowed."));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Society Photo is required."));
            }

            if (stpPhoto != null && !stpPhoto.isEmpty()) {
                if (!isValidImageType(stpPhoto)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Map.of("error", "Invalid STP Photo format. Only JPG,SVG, JPEG, or PNG files are allowed."));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "STP Photo is required."));
            }

            if (municipalOfficerPhoto != null && !municipalOfficerPhoto.isEmpty()) {
                if (!isValidImageType(municipalOfficerPhoto)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Map.of("error", "Invalid Municipal OfficerPhoto Photo format. Only JPG,SVG, JPEG, or PNG files are allowed."));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", " Municipal OfficerPhoto is required."));
            }

            STPForm stpForm = new STPForm();

            stpForm.setDate(dto.getDate());
            stpForm.setWardOffice(dto.getWardOffice());
            stpForm.setWardNo(dto.getWardNo());
            stpForm.setSocietyNo(dto.getSocietyNo());
            stpForm.setSocietyName(dto.getSocietyName());
            stpForm.setSocietyAddress(dto.getSocietyAddress());
            stpForm.setManagerName(dto.getManagerName());
            stpForm.setManagerMobile(dto.getManagerMobile());
            stpForm.setChairmanName(dto.getChairmanName());
            stpForm.setChairmanMobile(dto.getChairmanMobile());
            stpForm.setLatitude(dto.getLatitude());
            stpForm.setLongitude(dto.getLongitude());
            stpForm.setStpCapacity(dto.getStpCapacity());
            stpForm.setTechnology(dto.getTechnology());
            stpForm.setTotalTenements(dto.getTotalTenements());
            stpForm.setOccupiedTenements(dto.getOccupiedTenements());
            stpForm.setWaterConnectionDiameter(dto.getWaterConnectionDiameter());
            stpForm.setIsWaterReused(dto.getIsWaterReused());
            stpForm.setHasSeparateElectricity(dto.getHasSeparateElectricity());
            stpForm.setIsMSEBBeneficiary(dto.getIsMSEBBeneficiary());
            stpForm.setHasOnlineMonitoring(dto.getHasOnlineMonitoring());
            stpForm.setApiID(dto.getApiID());
            stpForm.setIsStpOperational(dto.getIsStpOperational());
            stpForm.setNonOperationalDays(dto.getNonOperationalDays());
            stpForm.setElectricityUnits(dto.getElectricityUnits());
            stpForm.setTreatedWaterUses(dto.getTreatedWaterUses());
            stpForm.setRemainingWaterDischarge(dto.getRemainingWaterDischarge());
            stpForm.setHasNearbyStormWaterLine(dto.getHasNearbyStormWaterLine());
            stpForm.setHasAppointedAgency(dto.getHasAppointedAgency());
            stpForm.setAgencyName(dto.getAgencyName());
            stpForm.setOtherReasons(dto.getOtherReasons());
            stpForm.setHasRWHSystem(dto.getHasRWHSystem());
            stpForm.setRwhPitsCount(dto.getRwhPitsCount());
            stpForm.setIsOWCOperational(dto.getIsOWCOperational());
            stpForm.setStpOperationalIssues(dto.getStpOperationalIssues());
            stpForm.setSocietyPhoto(societyPhoto.getBytes());
            stpForm.setStpPhoto(stpPhoto.getBytes());
            stpForm.setMunicipalOfficerPhoto(municipalOfficerPhoto.getBytes());
            stpForm.setStatus(true);
            stpForm.setCreatedUser(loginUser);
            stpForm.setModifiedUser(loginUser);
            stpForm.setModifiedDate(LocalDate.now());

            boolean saved = stpFormService.saveSTPForm(stpForm);
            if (saved) {
                return ResponseEntity.status(201).body(ExceptionMessages.STPFORM_SAVED);
            } else {
                return ResponseEntity.status(400).body(ExceptionMessages.STPFORM_NOT_SAVED);
            }

        } catch (Exception e) {
            log.error("An unexpected error occurred while addSTPForm(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id:\n "+ loginUser.getId());
            return ResponseEntity.badRequest().body(ExceptionMessages.SOMETHING_WENT_WRONG);
        }
    }


    @PutMapping("/updateStpForm/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<?> updateSTPForm(@PathVariable long id, @Valid @ModelAttribute STPFormDTO dto, @RequestParam("societyPhoto") MultipartFile societyPhoto,
                                                        @RequestParam("stpPhoto") MultipartFile stpPhoto, @RequestParam("municipalOfficerPhoto") MultipartFile municipalOfficerPhoto) {
        User loginUser = null;
        try {

            // Get the currently authenticated user
            loginUser = userService.getAuthenticateUser();
            if (loginUser == null) {
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }

            STPForm stpForm1 = stpFormService.getSTPFormById(id);

            if (stpForm1 == null) {
                return new ResponseEntity<>("STPForm not found with Id:" + id, HttpStatus.UNAUTHORIZED);
            }


            if (dto.getDate() == null) {
                return new ResponseEntity<>("Date is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getWardOffice() == null) {
                return new ResponseEntity<>("Word Office is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getWardNo() == null) {
                return new ResponseEntity<>("Word Number is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getSocietyNo() == null) {
                return new ResponseEntity<>("Society No is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getSocietyName() == null) {
                return new ResponseEntity<>("Society Name is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getSocietyAddress() == null) {
                return new ResponseEntity<>("Society Address is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getManagerName() == null) {
                return new ResponseEntity<>("Manager Name is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getManagerMobile() == null) {
                return new ResponseEntity<>(" Manager Mobile No is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getChairmanName() == null) {
                return new ResponseEntity<>("Chairman Name is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getChairmanMobile() == null) {
                return new ResponseEntity<>("Chairman Mobile No is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getLatitude() == null) {
                return new ResponseEntity<>("Latitude is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getLongitude() == null) {
                return new ResponseEntity<>("Longitude is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getStpCapacity() == null) {
                return new ResponseEntity<>("Stp Capacity is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getTechnology() == null) {
                return new ResponseEntity<>("Technology is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getTotalTenements() == null) {
                return new ResponseEntity<>("TotalTenements is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getOccupiedTenements() == null) {
                return new ResponseEntity<>("Occupied Tenements is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getWaterConnectionDiameter() == null) {
                return new ResponseEntity<>(" is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getIsWaterReused() == null) {
                return new ResponseEntity<>("Water Reused is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getHasSeparateElectricity() == null) {
                return new ResponseEntity<>("Separate Electricity is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getIsMSEBBeneficiary() == null) {
                return new ResponseEntity<>("MSEB Beneficiary is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getHasOnlineMonitoring() == null) {
                return new ResponseEntity<>("Online Monitoring is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getApiID() == null) {
                return new ResponseEntity<>("Api Id is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getIsStpOperational() == null) {
                return new ResponseEntity<>("Stp Operational is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getNonOperationalDays() == null) {
                return new ResponseEntity<>("NonOperational Days is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getElectricityUnits() == null) {
                return new ResponseEntity<>("Electricity Units is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getTreatedWaterUses() == null) {
                return new ResponseEntity<>("Treated Water Uses is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getRemainingWaterDischarge() == null) {
                return new ResponseEntity<>("Remaining Water Discharge is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getHasNearbyStormWaterLine() == null) {
                return new ResponseEntity<>("Near by Storm Water Line is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getHasAppointedAgency() == null) {
                return new ResponseEntity<>("Appointed Agency is empty", HttpStatus.BAD_REQUEST);
            }

            if (dto.getAgencyName() == null) {
                return new ResponseEntity<>("Agency Name is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getOtherReasons() == null) {
                return new ResponseEntity<>("OtherReasons is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getHasRWHSystem() == null) {
                return new ResponseEntity<>("RWH System is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getRwhPitsCount() == null) {
                return new ResponseEntity<>("Rwh Pits Count is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getIsOWCOperational() == null) {
                return new ResponseEntity<>("OWC Operational is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getStpOperationalIssues() == null) {
                return new ResponseEntity<>("Stp Operational Issues is empty", HttpStatus.BAD_REQUEST);
            }
            if (societyPhoto != null && !societyPhoto.isEmpty()) {
                if (!isValidImageType(societyPhoto)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Map.of("error", "Invalid Society Photo format. Only JPG,SVG, JPEG, or PNG files are allowed."));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Society Photo is required."));
            }

            if (stpPhoto != null && !stpPhoto.isEmpty()) {
                if (!isValidImageType(stpPhoto)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Map.of("error", "Invalid STP Photo format. Only JPG,SVG, JPEG, or PNG files are allowed."));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "STP Photo is required."));
            }

            if (municipalOfficerPhoto != null && !municipalOfficerPhoto.isEmpty()) {
                if (!isValidImageType(municipalOfficerPhoto)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Map.of("error", "Invalid Municipal OfficerPhoto Photo format. Only JPG,SVG, JPEG, or PNG files are allowed."));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", " Municipal OfficerPhoto is required."));
            }

            STPForm stpForm = stpForm1;

            stpForm.setDate(dto.getDate());
            stpForm.setWardOffice(dto.getWardOffice());
            stpForm.setWardNo(dto.getWardNo());
            stpForm.setSocietyNo(dto.getSocietyNo());
            stpForm.setSocietyName(dto.getSocietyName());
            stpForm.setSocietyAddress(dto.getSocietyAddress());
            stpForm.setManagerName(dto.getManagerName());
            stpForm.setManagerMobile(dto.getManagerMobile());
            stpForm.setChairmanName(dto.getChairmanName());
            stpForm.setChairmanMobile(dto.getChairmanMobile());
            stpForm.setLatitude(dto.getLatitude());
            stpForm.setLongitude(dto.getLongitude());
            stpForm.setStpCapacity(dto.getStpCapacity());
            stpForm.setTechnology(dto.getTechnology());
            stpForm.setTotalTenements(dto.getTotalTenements());
            stpForm.setOccupiedTenements(dto.getOccupiedTenements());
            stpForm.setWaterConnectionDiameter(dto.getWaterConnectionDiameter());
            stpForm.setIsWaterReused(dto.getIsWaterReused());
            stpForm.setHasSeparateElectricity(dto.getHasSeparateElectricity());
            stpForm.setIsMSEBBeneficiary(dto.getIsMSEBBeneficiary());
            stpForm.setHasOnlineMonitoring(dto.getHasOnlineMonitoring());
            stpForm.setApiID(dto.getApiID());
            stpForm.setIsStpOperational(dto.getIsStpOperational());
            stpForm.setNonOperationalDays(dto.getNonOperationalDays());
            stpForm.setElectricityUnits(dto.getElectricityUnits());
            stpForm.setTreatedWaterUses(dto.getTreatedWaterUses());
            stpForm.setRemainingWaterDischarge(dto.getRemainingWaterDischarge());
            stpForm.setHasNearbyStormWaterLine(dto.getHasNearbyStormWaterLine());
            stpForm.setHasAppointedAgency(dto.getHasAppointedAgency());
            stpForm.setAgencyName(dto.getAgencyName());
            stpForm.setOtherReasons(dto.getOtherReasons());
            stpForm.setHasRWHSystem(dto.getHasRWHSystem());
            stpForm.setRwhPitsCount(dto.getRwhPitsCount());
            stpForm.setIsOWCOperational(dto.getIsOWCOperational());
            stpForm.setStpOperationalIssues(dto.getStpOperationalIssues());
            stpForm.setSocietyPhoto(societyPhoto.getBytes());
            stpForm.setStpPhoto(stpPhoto.getBytes());
            stpForm.setMunicipalOfficerPhoto(municipalOfficerPhoto.getBytes());
            stpForm.setStatus(true);
            stpForm.setCreatedUser(loginUser);
            stpForm.setModifiedUser(loginUser);
            stpForm.setModifiedDate(LocalDate.now());

            boolean isUpdated = stpFormService.saveSTPForm(stpForm);
            if (isUpdated) {
                return ResponseEntity.ok().body(ExceptionMessages.STPFORM_UPDATE);
            } else {
                return ResponseEntity.status(400).body(ExceptionMessages.STPFORM_NOT_UPDATE);
            }

        } catch (Exception e) {
            log.error("An unexpected error occurred while updateSTPForm(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id:\n "+ loginUser.getId());
            return ResponseEntity.badRequest().body(ExceptionMessages.SOMETHING_WENT_WRONG);
        }

    }


    @GetMapping("/getStpFormBySocietyName/{societyName}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<?> getSTPFormBySocietyName(@PathVariable  String societyName)
    {
        User loginUser = null;
      try
      {
           loginUser = userService.getAuthenticateUser();
          if (loginUser == null)
          {
              return new ResponseEntity<>("User is not authenticated",HttpStatus.UNAUTHORIZED);
          }

          if(societyName == null || societyName.trim().isEmpty())
          {
              return new ResponseEntity<>("Society name cannot be empty",HttpStatus.BAD_REQUEST);
          }

          List<STPForm> stpFormsBySocietyName = stpFormService.getSTPFormsBySocietyName(societyName);

          if (stpFormsBySocietyName.isEmpty())
          {
              return new ResponseEntity<>("No STP forms found for society: " + societyName,HttpStatus.NOT_FOUND);
          }

          return ResponseEntity.ok(stpFormsBySocietyName);

      } catch (Exception e) {
          log.error("An unexpected error occurred while getSTPFormBySocietyName(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id:\n "+ loginUser.getId());
          return ResponseEntity.badRequest().body(ExceptionMessages.SOMETHING_WENT_WRONG);
      }
    }


    @GetMapping("/allSTPForm/list")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<?> getAllSTPForm() {
        User loginUser = null;

        try {

            // Fetch the authenticated user from the user service
            loginUser = userService.getAuthenticateUser();
            if (loginUser == null) {
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }

            List<STPForm> stpForm = stpFormService.findAllSTPForm();
            if (stpForm == null) {
                return new ResponseEntity<>("STP Form list is empty", HttpStatus.NOT_FOUND);
            }

            return ResponseEntity.ok(stpForm);

        } catch (Exception e) {
            log.error("An unexpected error occurred while getAllSTPForm(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id:\n "+ loginUser.getId());
            return new ResponseEntity<>("STPForm not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/download/stpPhoto/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<?> downloadStpPhoto(@PathVariable long id) {
        User loginUser = null;
        try {
             loginUser = userService.getAuthenticateUser();
            if (loginUser == null) {
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }

            STPForm stpForm = stpFormService.getSTPFormById(id);
            if (stpForm == null || stpForm.getStpPhoto() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("STP Photo not found for STP Form ID: " + id);
            }

            return buildImageResponse(stpForm.getStpPhoto(), "stp_photo_" + id);
        } catch (Exception e) {
            log.error("An unexpected error occurred while downloadStpPhoto(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id:\n "+ loginUser.getId());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to download STP photo: " + e.getMessage());
        }
    }

    @GetMapping("/download/municipalOfficerPhoto/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<?> downloadMunicipalOfficerPhoto(@PathVariable long id) {
        User loginUser = null;
        try {
             loginUser = userService.getAuthenticateUser();
            if (loginUser == null) {
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }

            STPForm stpForm = stpFormService.getSTPFormById(id);
            if (stpForm == null || stpForm.getMunicipalOfficerPhoto() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Municipal Officer Photo not found for STP Form ID: " + id);
            }

            return buildImageResponse(stpForm.getMunicipalOfficerPhoto(), "municipal_officer_photo_" + id);
        } catch (Exception e) {
            log.error("An unexpected error occurred while downloadMunicipalOfficerPhoto(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id:\n "+ loginUser.getId());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to download municipal officer photo: " + e.getMessage());
        }
    }



    @GetMapping("/download/societyPhoto/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<?> downloadSocietyPhoto(@PathVariable long id) {
        User loginUser = null;
        try {
             loginUser = userService.getAuthenticateUser();
            if (loginUser == null) {
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }

            STPForm stpForm = stpFormService.getSTPFormById(id);
            if (stpForm == null || stpForm.getSocietyPhoto() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Society Photo not found for STP Form ID: " + id);
            }

            return buildImageResponse(stpForm.getSocietyPhoto(), "society_photo_" + id);
        } catch (Exception e) {
            log.error("An unexpected error occurred while downloadSocietyPhoto(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id:\n "+ loginUser.getId());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to download society photo: " + e.getMessage());
        }
    }


    @GetMapping("/downloadStpForm/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<?> downloadStpForm(@PathVariable long id) {
        User loginUser = null;
        try {

            // Fetch the authenticated user from the user service
            loginUser = userService.getAuthenticateUser();
            if (loginUser == null) {
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }

            // Get the STPForm by ID
            STPForm stpForm = stpFormService.getSTPFormById(id);

            if (stpForm == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No STP Form found with ID: " + id);
            }

            // Generate PDF (you'll need to implement this)
            byte[] pdfBytes = generateStpFormPdf(stpForm);

            if (pdfBytes == null || pdfBytes.length == 0) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            // Prepare response
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(
                    ContentDisposition.attachment()
                            .filename("STP_Form_" + id + ".pdf")
                            .build());
            headers.setContentLength(pdfBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (Exception e) {
            log.error("An unexpected error occurred while downloadStpForm(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id:\n "+ loginUser.getId());
            return ResponseEntity.status(404).body(ExceptionMessages.SOMETHING_WENT_WRONG);

        }
    }

    private byte[] generateStpFormPdf(STPForm stpForm) {

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // 1. Create PDF document
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            // 2. Add content to PDF
            document.add(new Paragraph("STP Form Details"));
            document.add(new Paragraph("Society Name: " + stpForm.getSocietyName()));
            document.add(new Paragraph("Ward No: " + stpForm.getWardNo()));
            document.add(new Paragraph("Date: " + stpForm.getDate()));
            document.add(new Paragraph("Ward Office: " + stpForm.getWardOffice()));
            document.add(new Paragraph("Society No: " + stpForm.getSocietyNo()));
            document.add(new Paragraph("Society Address: " + stpForm.getSocietyAddress()));
            document.add(new Paragraph("Manager Name: " + stpForm.getManagerName()));
            document.add(new Paragraph("Manager Mobile No: " + stpForm.getManagerMobile()));
            document.add(new Paragraph("Chairman Name: " + stpForm.getChairmanName()));
            document.add(new Paragraph("Chairman Mobile No: " + stpForm.getChairmanMobile()));
            document.add(new Paragraph("Latitude: " + stpForm.getLatitude()));
            document.add(new Paragraph("Longitude: " + stpForm.getLongitude()));
            document.add(new Paragraph("Stp Capacity: " + stpForm.getStpCapacity()));
            document.add(new Paragraph("Technology: " + stpForm.getTechnology()));
            document.add(new Paragraph("Total Tenements: " + stpForm.getTotalTenements()));
            document.add(new Paragraph("Occupied Tenements: " + stpForm.getOccupiedTenements()));
            document.add(new Paragraph("Water Connection Diameter: " + stpForm.getWaterConnectionDiameter()));
            document.add(new Paragraph("Water Reused: " + stpForm.getIsWaterReused()));
            document.add(new Paragraph("Separate Electricity: " + stpForm.getHasSeparateElectricity()));
            document.add(new Paragraph("MSEB Beneficiary: " + stpForm.getIsMSEBBeneficiary()));
            document.add(new Paragraph("Online Monitoring: " + stpForm.getHasOnlineMonitoring()));
            document.add(new Paragraph("Api ID: " + stpForm.getApiID()));
            document.add(new Paragraph("Stp Operational: " + stpForm.getIsStpOperational()));
            document.add(new Paragraph("Non Operational Days: " + stpForm.getNonOperationalDays()));
            document.add(new Paragraph("Electricity Units: " + stpForm.getElectricityUnits()));
            document.add(new Paragraph("TreatedWater Uses: " + stpForm.getTreatedWaterUses()));
            document.add(new Paragraph("RemainingWater Discharge: " + stpForm.getRemainingWaterDischarge()));
            document.add(new Paragraph("Near by Storm WaterLine: " + stpForm.getHasNearbyStormWaterLine()));
            document.add(new Paragraph("Appointed Agency: " + stpForm.getHasAppointedAgency()));
            document.add(new Paragraph("Agency Name: " + stpForm.getAgencyName()));
            document.add(new Paragraph("Other Reasons: " + stpForm.getOtherReasons()));
            document.add(new Paragraph("RWH System: " + stpForm.getHasRWHSystem()));
            document.add(new Paragraph("Rwh Pits Count: " + stpForm.getRwhPitsCount()));
            document.add(new Paragraph("OWC Operational: " + stpForm.getIsOWCOperational()));
            document.add(new Paragraph("Operational Issues: " + stpForm.getStpOperationalIssues()));

            // Close document
            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/getSTPFormbyId/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<?> getSTPFormbyId(@PathVariable long id) {
        User loginUser = null;

        try {

            // Fetch the authenticated user from the user service
            loginUser = userService.getAuthenticateUser();
            if (loginUser == null) {
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }

            STPForm stpFormById = stpFormService.getSTPFormById(id);
            if (stpFormById == null) {
                return new ResponseEntity<>("STPForm with ID " + id + " not found", HttpStatus.NOT_FOUND);
            }

            return ResponseEntity.ok(stpFormById);

        } catch (Exception e) {
            log.error("An unexpected error occurred while getSTPFormbyId(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id:\n "+ loginUser.getId());
            return new ResponseEntity<>("STPForm with ID " + id + " not found", HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/disable/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<String> disableStpForm(@PathVariable long id) {
        User loginUser = null;
        try {
            loginUser = userService.getAuthenticateUser();
            if (loginUser == null) {
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }
            STPForm stpForm = stpFormService.deleteStpForm(id);
            if (stpForm == null) {
                return new ResponseEntity<>("Stp Form not found", HttpStatus.NOT_FOUND);
            }

            if (!stpForm.isStatus()) {
                return new ResponseEntity<>("Stp Form is already deactivate.", HttpStatus.BAD_REQUEST);
            }
            stpForm.setStatus(false);
            stpForm.setModifiedDate(LocalDate.now());
            stpFormService.saveSTPForm(stpForm);
            return new ResponseEntity<>("Stp Form with ID " + id + " has been disabled successfully.", HttpStatus.OK);

        } catch (Exception e) {
            log.error("An unexpected error occurred while disableStpForm(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id:\n "+ loginUser.getId());
            return ResponseEntity.badRequest().body(ExceptionMessages.SOMETHING_WENT_WRONG);
        }
    }

    @GetMapping("/list/activeSTPForm")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<?> listActiveSTPForm() {
        User loginUser = null;
        try {
            loginUser = userService.getAuthenticateUser();
            if (loginUser == null) {
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }
            List<STPForm> allSTPForm = stpFormService.findActiveStpForm();
            if (allSTPForm == null) {
                return new ResponseEntity<>("No STP Form Available", HttpStatus.BAD_REQUEST);
            }
            return ResponseEntity.ok(allSTPForm);
        } catch (Exception e) {
            log.error("An unexpected error occurred while listActiveSTPForm(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id:\n "+ loginUser.getId());
            return ResponseEntity.badRequest().body(ExceptionMessages.SOMETHING_WENT_WRONG);
        }
    }

    private boolean isValidImageType(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return false;
            }

            String contentType = file.getContentType();

            return contentType != null &&
                    (contentType.equals("image/jpeg") ||
                            contentType.equals("image/png") ||
                            contentType.equals("image/jpg") ||
                            contentType.equals("image/svg"));
        } catch (Exception e) {
            return false;
        }


    }

    // Helper method to build image response
    private ResponseEntity<?> buildImageResponse(byte[] imageBytes, String filename) {
        if (imageBytes == null || imageBytes.length == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        // Determine content type (e.g., "image/jpeg", "image/png")
        String contentType = determineContentType(imageBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(filename + getFileExtension(contentType))
                        .build());
        headers.setContentLength(imageBytes.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ByteArrayResource(imageBytes));
    }

    // Helper method to determine content type (simplified)
    private String determineContentType(byte[] imageBytes) {
        // Simple check for JPEG/PNG (for better accuracy, use a library like Apache Tika)
        if (imageBytes.length >= 2) {
            if (imageBytes[0] == (byte) 0xFF && imageBytes[1] == (byte) 0xD8) {
                return "image/jpeg";
            } else if (imageBytes[0] == (byte) 0x89 && imageBytes[1] == (byte) 0x50) { // PNG
                return "image/png";
            }
        }
        return "application/octet-stream"; // Default if unknown
    }

    // Helper method to get file extension
    private String getFileExtension(String contentType) {
        switch (contentType) {
            case "image/jpeg":
                return ".jpg";
            case "image/png":
                return ".png";
            case "image/svg+xml":
                return ".svg";
            default:
                return "";
        }
    }


}
