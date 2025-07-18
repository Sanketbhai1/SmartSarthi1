package com.pcmc.smartsarathi.controller;


import com.pcmc.smartsarathi.dto.PrivateStpDTO;
import com.pcmc.smartsarathi.dto.PrivateStpResponseDTO;
import com.pcmc.smartsarathi.model.PrivateStp;
import com.pcmc.smartsarathi.model.User;
import com.pcmc.smartsarathi.model.Zone;
import com.pcmc.smartsarathi.service.PrivateStpService;
import com.pcmc.smartsarathi.service.UserService;
import com.pcmc.smartsarathi.service.ZoneService;
import com.pcmc.smartsarathi.utils.ExceptionMessages;
import com.pcmc.smartsarathi.utils.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/privateStp")
public class PrivateStpController {

    @Autowired
    private PrivateStpService privateStpService;

    @Autowired
    private UserService userService;

    @Autowired
    private ZoneService zoneService;

    private static final Logger log = Logger.getLogger(PrivateStpController.class);

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<String> addPrivateStp(@RequestBody PrivateStpDTO privateStpDTO) {
        User loginUser = null;
        try {
            loginUser = userService.getAuthenticateUser();
            if (loginUser == null) {
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }

            Zone zone = zoneService.findByTitle(privateStpDTO.getTitle());
            if (zone == null) {
                return new ResponseEntity<>("Zone not found", HttpStatus.BAD_REQUEST);
            }

            if (privateStpDTO.getLocationName() == null) {
                return new ResponseEntity<>("Location name is empty", HttpStatus.BAD_REQUEST);
            }
            if (privateStpDTO.getInstalledCapacity() == null) {
                return new ResponseEntity<>("Installed Capacity is empty", HttpStatus.BAD_REQUEST);
            }

            if (privateStpDTO.getNoOfTenaments() == null) {
                return new ResponseEntity<>("No Of Tenaments is empty", HttpStatus.BAD_REQUEST);
            }

            if (privateStpDTO.getFlow() == null) {
                return new ResponseEntity<>("Flow is empty", HttpStatus.BAD_REQUEST);
            }

            if (privateStpDTO.getPh() == null) {
                return new ResponseEntity<>("PH is empty", HttpStatus.BAD_REQUEST);
            }

            if (privateStpDTO.getDoLevel() == null) {
                return new ResponseEntity<>("do is empty", HttpStatus.BAD_REQUEST);
            }

            if (privateStpDTO.getBod() == null) {
                return new ResponseEntity<>("Bod is empty", HttpStatus.BAD_REQUEST);
            }

            if (privateStpDTO.getCod() == null) {
                return new ResponseEntity<>("Cod is empty", HttpStatus.BAD_REQUEST);
            }

            if (privateStpDTO.getTss() == null) {
                return new ResponseEntity<>("Tss is empty", HttpStatus.BAD_REQUEST);
            }

            if (privateStpDTO.getAddedTime() == null) {
                return new ResponseEntity<>("Insert Date  is empty", HttpStatus.BAD_REQUEST);
            }

            if(privateStpDTO.getStpOms() == null)
            {
                return new ResponseEntity<>("Stp Oms is empty",HttpStatus.BAD_REQUEST);
            }

            if(privateStpDTO.getStpWorking() == null)
            {
                return new ResponseEntity<>("Stp Working is empty ",HttpStatus.BAD_REQUEST);
            }



            PrivateStp privateStp = new PrivateStp();
            privateStp.setZone(zone);
            privateStp.setLocationName(privateStpDTO.getLocationName());
            privateStp.setFlow(Double.parseDouble(privateStpDTO.getFlow()));
            privateStp.setDoLevel(Double.parseDouble(privateStpDTO.getDoLevel()));
            privateStp.setBod(Double.parseDouble(privateStpDTO.getBod()));
            privateStp.setAddedTime(privateStpDTO.getAddedTime());
            privateStp.setInstalledCapacity(privateStpDTO.getInstalledCapacity());
            privateStp.setCod(Double.parseDouble(privateStpDTO.getCod()));
            privateStp.setTss(Double.parseDouble(privateStpDTO.getTss()));
            privateStp.setNoOfTenaments(privateStpDTO.getNoOfTenaments());
            privateStp.setPh(Double.parseDouble(privateStpDTO.getPh()));
            privateStp.setOms(privateStpDTO.getStpOms());
            privateStp.setWorking(privateStpDTO.getStpWorking());
            privateStp.setStatus(true);
            privateStp.setCreatedUser(loginUser);
            privateStp.setModifiedUser(loginUser);
            privateStp.setAddedTime(LocalDate.now());
            privateStp.setModifiedTime(LocalDate.now());


            boolean saved = privateStpService.save(privateStp);
            if (saved) {
                return ResponseEntity.status(201).body(ExceptionMessages.PRIVATE_STP_SAVED);
            } else {
                return ResponseEntity.status(400).body(ExceptionMessages.PRIVATE_STP_NOT_SAVED);
            }
        } catch (Exception e) {
            log.error("An unexpected error occurred while addPrivateStp(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id:\n "+ loginUser.getId());
            return ResponseEntity.badRequest().body(ExceptionMessages.SOMETHING_WENT_WRONG);
        }
    }


    @GetMapping("/list")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<?> listPrivateStp() {
        User loginUser = null;
        try {
            loginUser = userService.getAuthenticateUser();
            if (loginUser == null) {
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }

            List<PrivateStp> privateStps = privateStpService.getAllPrivateStps();
            if (privateStps.isEmpty()) {
                return ResponseEntity.status(404).body(" No Active Private STP available.");
            }

            List<PrivateStpResponseDTO> privateStpResponseDTOStream = privateStps.stream().map(privateStp -> {
                PrivateStpResponseDTO dto = new PrivateStpResponseDTO();
                dto.setId(privateStp.getId());
                dto.setLocationName(privateStp.getLocationName());
                dto.setFlow(privateStp.getFlow());
                dto.setDoLevel(privateStp.getDoLevel());
                dto.setBod(privateStp.getBod());
                dto.setCod(privateStp.getCod());
                dto.setTss(privateStp.getTss());
                dto.setPh(privateStp.getPh());
                dto.setInstalledCapacity(privateStp.getInstalledCapacity());
                dto.setNoOfTenaments(privateStp.getNoOfTenaments());
                dto.setAddedTime(privateStp.getAddedTime());
                dto.setStpOms(privateStp.isOms());
                dto.setStpWorking(privateStp.isWorking());
                dto.setTitle(privateStp.getZone() != null ? privateStp.getZone().getTitle() : null); // Set only zone name
                return dto;

            }).collect(Collectors.toList());

            return ResponseEntity.ok(privateStpResponseDTOStream);
        } catch (Exception e) {
            log.error("An unexpected error occurred while listPrivateStp(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id:\n "+ loginUser.getId());
            return ResponseEntity.badRequest().body(ExceptionMessages.SOMETHING_WENT_WRONG);
        }
    }

    // Endpoint to get all Private STPs by Zone Title
    @GetMapping("/zone/{zoneTitle}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<?> getPrivateStpsByZoneTitle(@PathVariable String zoneTitle) {
        User loginUser = null;
        try {

            loginUser = userService.getAuthenticateUser();
            if (loginUser == null) {
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }

            List<PrivateStp> privateStps = privateStpService.getPrivateStpsByZoneTitle(zoneTitle);
            if (privateStps.isEmpty()) {
                return ResponseEntity.status(404).body("No Private STP found for the zone: " + zoneTitle);
            }

            List<PrivateStpResponseDTO> privateStpResponseDTOList = privateStps.stream().map(privateStp -> {

                PrivateStpResponseDTO dto = new PrivateStpResponseDTO();
                dto.setId(privateStp.getId());
                dto.setLocationName(privateStp.getLocationName());
                dto.setFlow(privateStp.getFlow());
                dto.setDoLevel(privateStp.getDoLevel());
                dto.setBod(privateStp.getBod());
                dto.setCod(privateStp.getCod());
                dto.setTss(privateStp.getTss());
                dto.setPh(privateStp.getPh());
                dto.setInstalledCapacity(privateStp.getInstalledCapacity());
                dto.setNoOfTenaments(privateStp.getNoOfTenaments());
                dto.setAddedTime(privateStp.getAddedTime());
                dto.setStpOms(privateStp.isOms());
                dto.setStpWorking(privateStp.isWorking());
                dto.setTitle(privateStp.getZone() != null ? privateStp.getZone().getTitle() : null); // Set only zone name
                return dto;

            }).collect(Collectors.toList());

            return ResponseEntity.ok(privateStpResponseDTOList);
        } catch (Exception e) {
            log.error("An unexpected error occurred while getPrivateStpsByZoneTitle(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id:\n "+ loginUser.getId());
            return ResponseEntity.badRequest().body(ExceptionMessages.SOMETHING_WENT_WRONG);
        }
    }


    @PutMapping("/disable/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<String> disablePrivateStp(@PathVariable long id) {
        User loginUser = null;
        try {
            loginUser = userService.getAuthenticateUser();
            if (loginUser == null) {
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }
            PrivateStp privateStp = privateStpService.deletePrivateStp(id);
            if (privateStp == null) {
                return new ResponseEntity<>("Private STP not found", HttpStatus.BAD_REQUEST);
            }

            if (!privateStp.isStatus()) {
                return new ResponseEntity<>("Private STP is already deactivate.", HttpStatus.BAD_REQUEST);
            }
            privateStp.setStatus(false);
            privateStp.setModifiedTime(LocalDate.now());
            privateStpService.save(privateStp);
            return new ResponseEntity<>("Private STP with ID " + id + " has been disabled successfully.", HttpStatus.OK);

        } catch (Exception e) {
            log.error("An unexpected error occurred while disablePrivateStp(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id:\n "+ loginUser.getId());
            return ResponseEntity.badRequest().body(ExceptionMessages.SOMETHING_WENT_WRONG);
        }
    }

    @PutMapping("/updatePrivateStp/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<String> updatePrivateStp(@PathVariable long id, @RequestBody PrivateStpDTO privateStpDTO) {
        User loginUser = null;
        try {
             loginUser = userService.getAuthenticateUser();
            if (loginUser == null) {
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }

            PrivateStp existingStp = privateStpService.getById(id);
            if (existingStp == null) {
                return new ResponseEntity<>("Private STP not found with id " + id, HttpStatus.NOT_FOUND);
            }

            // Validate zone if title is provided
            if (privateStpDTO.getTitle() != null) {
                Zone zone = zoneService.findByTitle(privateStpDTO.getTitle());
                if (zone == null) {
                    return new ResponseEntity<>("Zone not found", HttpStatus.BAD_REQUEST);
                }
                existingStp.setZone(zone);
            }

            // Validation checks (same as add endpoint)
            if (privateStpDTO.getLocationName() == null) {
                return new ResponseEntity<>("Location name is empty", HttpStatus.BAD_REQUEST);
            }
            if (privateStpDTO.getInstalledCapacity() == null) {
                return new ResponseEntity<>("Installed Capacity is empty", HttpStatus.BAD_REQUEST);
            }
            if (privateStpDTO.getNoOfTenaments() == null) {
                return new ResponseEntity<>("No Of Tenaments is empty", HttpStatus.BAD_REQUEST);
            }
            if (privateStpDTO.getFlow() == null) {
                return new ResponseEntity<>("Flow is empty", HttpStatus.BAD_REQUEST);
            }
            if (privateStpDTO.getPh() == null) {
                return new ResponseEntity<>("PH is empty", HttpStatus.BAD_REQUEST);
            }
            if (privateStpDTO.getDoLevel() == null) {
                return new ResponseEntity<>("do is empty", HttpStatus.BAD_REQUEST);
            }
            if (privateStpDTO.getBod() == null) {
                return new ResponseEntity<>("Bod is empty", HttpStatus.BAD_REQUEST);
            }
            if (privateStpDTO.getCod() == null) {
                return new ResponseEntity<>("Cod is empty", HttpStatus.BAD_REQUEST);
            }
            if (privateStpDTO.getTss() == null) {
                return new ResponseEntity<>("Tss is empty", HttpStatus.BAD_REQUEST);
            }
            if (privateStpDTO.getAddedTime() == null) {
                return new ResponseEntity<>("Insert Date is empty", HttpStatus.BAD_REQUEST);
            }
            if (privateStpDTO.getStpOms() == null) {
                return new ResponseEntity<>("Stp Oms is empty", HttpStatus.BAD_REQUEST);
            }
            if (privateStpDTO.getStpWorking() == null) {
                return new ResponseEntity<>("Stp Working is empty", HttpStatus.BAD_REQUEST);
            }

            // Update fields
            existingStp.setLocationName(privateStpDTO.getLocationName());
            existingStp.setFlow(Double.parseDouble(privateStpDTO.getFlow()));
            existingStp.setDoLevel(Double.parseDouble(privateStpDTO.getDoLevel()));
            existingStp.setBod(Double.parseDouble(privateStpDTO.getBod()));
            existingStp.setAddedTime(privateStpDTO.getAddedTime());
            existingStp.setInstalledCapacity(privateStpDTO.getInstalledCapacity());
            existingStp.setCod(Double.parseDouble(privateStpDTO.getCod()));
            existingStp.setTss(Double.parseDouble(privateStpDTO.getTss()));
            existingStp.setNoOfTenaments(privateStpDTO.getNoOfTenaments());
            existingStp.setPh(Double.parseDouble(privateStpDTO.getPh()));
            existingStp.setOms(privateStpDTO.getStpOms());
            existingStp.setWorking(privateStpDTO.getStpWorking());
            existingStp.setModifiedUser(loginUser);
            existingStp.setModifiedTime(LocalDate.now());

            boolean isUpdated = privateStpService.save(existingStp);
            if (isUpdated) {
                return ResponseEntity.status(HttpStatus.OK).body("Private STP updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update Private STP");
            }
        } catch (Exception e) {
            log.error("An unexpected error occurred while updatePrivateStp(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id:\n "+ loginUser.getId());
            return ResponseEntity.badRequest().body(ExceptionMessages.SOMETHING_WENT_WRONG);
        }
    }


}
