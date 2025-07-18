package com.pcmc.smartsarathi.controller;

import com.pcmc.smartsarathi.dto.ZoneDTO;
import com.pcmc.smartsarathi.model.User;
import com.pcmc.smartsarathi.model.Zone;
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

@RestController
@RequestMapping("/zone")
public class ZoneController {

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private UserService userService;

    private static final Logger log = Logger.getLogger(ZoneController.class);


    @PostMapping("/add")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<String> addZone(@RequestBody ZoneDTO dto) {
        User loginUser = null;
        try {
            loginUser = userService.getAuthenticateUser();
            if (loginUser == null) {
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }


            if (dto.getTitle() == null) {
                return new ResponseEntity<>("Zone name is empty", HttpStatus.BAD_REQUEST);
            }
            if (dto.getCode() == null) {
                return new ResponseEntity<>("Zone code is empty", HttpStatus.BAD_REQUEST);
            }

            if(dto.getStatus() == null){
               return new ResponseEntity<>(" Zone Status is empty",HttpStatus.BAD_REQUEST);
            }

            Zone existingZone = zoneService.findByTitle(dto.getTitle());
            if(existingZone != null)
            {
                return new ResponseEntity<>("Zone already exists. Using existing zone",HttpStatus.CONFLICT);
            }


            Zone zone = new Zone();
            zone.setTitle(dto.getTitle());
            zone.setCode(dto.getCode());
            zone.setStatus(dto.getStatus());
            zone.setAddedTime(LocalDate.now());
            zone.setModifiedTime(LocalDate.now());
            zone.setCreatedUser(loginUser);
            zone.setModifiedUser(loginUser);

            boolean saved = zoneService.saveZone(zone);
            if (saved) {
                return ResponseEntity.status(201).body(ExceptionMessages.ZONE_SAVED);
            } else {
                return ResponseEntity.badRequest().body(ExceptionMessages.ZONE_NOT_SAVED);
            }

        } catch (Exception e) {
            log.error("An unexpected error occurred while addZone(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id:\n "+ loginUser.getId());
            return ResponseEntity.badRequest().body(ExceptionMessages.SOMETHING_WENT_WRONG);
        }
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<?> listZones() {
        User loginUser = null;
        try {
            loginUser = userService.getAuthenticateUser();
            if (loginUser == null) {
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }
            List<Zone> allZones = zoneService.findActiveZone();
            return ResponseEntity.ok(allZones);
        } catch (Exception e) {
            log.error("An unexpected error occurred while listZones(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id:\n "+ loginUser.getId());
            return ResponseEntity.badRequest().body(ExceptionMessages.SOMETHING_WENT_WRONG);
        }
    }


    @PutMapping("/disable/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<String> disableZone(@PathVariable long id) {
        User loginUser = null;
        try {
            loginUser = userService.getAuthenticateUser();
            if (loginUser == null) {
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }
            Zone zone = zoneService.deleteZone(id);
            if (zone == null) {
                return new ResponseEntity<>("Zone not found", HttpStatus.NOT_FOUND);
            }

            if (Boolean.FALSE.equals(zone.getStatus())) {
                return new ResponseEntity<>("Zone is already deactivate.", HttpStatus.BAD_REQUEST);
            }
            zone.setStatus(false);
            zone.setModifiedTime(LocalDate.now());
            zoneService.saveZone(zone);
            return new ResponseEntity<>("Zone with ID " + id + " has been disabled successfully.", HttpStatus.OK);

        } catch (Exception e) {
            log.error("An unexpected error occurred while disableZone(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id:\n "+ loginUser.getId());
            return ResponseEntity.badRequest().body(ExceptionMessages.SOMETHING_WENT_WRONG);
        }
    }

    @PutMapping("/updateZone/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<?> updateZone(@PathVariable long id, @RequestBody ZoneDTO zoneDTO) {
        User loginUser = null;
        try {
            loginUser = userService.getAuthenticateUser();
            if (loginUser == null) {
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }
            Zone zone = zoneService.getZoneById(id);
            if (zone == null) {
                return new ResponseEntity<>("Zone not found with id " + id, HttpStatus.BAD_REQUEST);
            }
            if (zoneDTO.getTitle() == null) {
                return new ResponseEntity<>("Zone name is empty", HttpStatus.BAD_REQUEST);
            }
            if (zoneDTO.getCode() == null) {
                return new ResponseEntity<>("Zone code is empty", HttpStatus.BAD_REQUEST);
            }

            if(zoneDTO.getStatus()== null)
            {
                return new ResponseEntity<>("Zone Code is empty",HttpStatus.BAD_REQUEST);
            }

            Zone zones = zone;

            zones.setTitle(zoneDTO.getTitle());
            zones.setCode(zoneDTO.getCode());
            zones.setStatus(zones.getStatus());

            boolean updateZone = zoneService.saveZone(zones);
            if (updateZone) {
                return new ResponseEntity<>("Zone updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to update zone", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            log.error("An unexpected error occurred while updateZone(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id:\n "+ loginUser.getId());
            return ResponseEntity.badRequest().body(ExceptionMessages.SOMETHING_WENT_WRONG);
        }

    }
}


