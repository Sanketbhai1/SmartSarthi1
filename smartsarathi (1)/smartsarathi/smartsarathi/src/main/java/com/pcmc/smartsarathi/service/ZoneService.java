package com.pcmc.smartsarathi.service;

import com.pcmc.smartsarathi.model.Zone;
import com.pcmc.smartsarathi.repository.ZoneRepository;
import com.pcmc.smartsarathi.utils.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneService {

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private UserService userService;

    private static final Logger log = Logger.getLogger(ZoneService.class);

    public static Boolean status;

    public List<Zone> getAllZones() {
        try {
            return zoneRepository.findAll();
        } catch (Exception e) {
            log.error("An unexpected error occurred while getAllZones" + ExceptionUtils.getStackTrace(e) +"Logged User" + userService.getAuthenticateUser().getId());
            return null;
        }

    }


    public boolean saveZone(Zone zone) {
        try {
            zoneRepository.save(zone);
            return true;
        } catch (Exception e) {
            log.error("An unexpected error occurred while saveZone" + ExceptionUtils.getStackTrace(e) +"Logged User" + userService.getAuthenticateUser().getId());
            return false;
        }
    }

    public Zone getZoneById(long id) {
        try {
            return zoneRepository.findById(id).orElse(null);
        } catch (Exception e) {
            log.error("An unexpected error occurred while getZoneById" + ExceptionUtils.getStackTrace(e) +"Logged User" + userService.getAuthenticateUser().getId());
            return null;
        }
    }

    public Zone deleteZone(long id) {
        try {
            return zoneRepository.findById(id).orElse(null);
        } catch (Exception e) {
            log.error("An unexpected error occurred while deleteZone" + ExceptionUtils.getStackTrace(e) +"Logged User" + userService.getAuthenticateUser().getId());
            return null;
        }
    }

    public List<Zone> findActiveZone() {
        status = true;
        try {
            List<Zone> byStatus = zoneRepository.findByStatus(status);
            return byStatus;
        } catch (Exception e) {
            log.error("An unexpected error occurred while findActiveZone" + ExceptionUtils.getStackTrace(e) +"Logged User" + userService.getAuthenticateUser().getId());
            return null;
        }
    }


    public boolean enableZone(long id) {
        Zone zone = getZoneById(id);
        if (zone != null) {
            zone.setStatus(true);
            zoneRepository.save(zone);
            return true;
        }
        return false;
    }

    public Zone findByTitle(String title) {
        try {
            return zoneRepository.findByTitleIgnoreCase(title);
        } catch (Exception e) {
            log.error("An unexpected error occurred while findByTitle" + ExceptionUtils.getStackTrace(e) +"Logged User" + userService.getAuthenticateUser().getId());
            return null;
        }
    }


}
