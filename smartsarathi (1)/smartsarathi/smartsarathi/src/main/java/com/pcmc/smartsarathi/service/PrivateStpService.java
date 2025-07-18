package com.pcmc.smartsarathi.service;


import com.pcmc.smartsarathi.model.PrivateStp;

import com.pcmc.smartsarathi.repository.PrivateStpRepository;
import com.pcmc.smartsarathi.repository.UserRepository;
import com.pcmc.smartsarathi.repository.ZoneRepository;
import com.pcmc.smartsarathi.utils.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrivateStpService {

    @Autowired
    private PrivateStpRepository privateStpRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private static final Logger log = Logger.getLogger(PrivateStpService.class);


    static boolean status;

    public List<PrivateStp> getAll() {
        try {
            return privateStpRepository.findAll();
        } catch (Exception e) {
            log.error("An unexpected error occurred while getAll" + ExceptionUtils.getStackTrace(e) +"Logged User" + userService.getAuthenticateUser().getId());

            return null;
        }
    }

    // Get all Private STPs with Zone Name
    public List<PrivateStp> getAllPrivateStps() {
        try {
            status = true;
            return privateStpRepository.findByStatus(status);
        } catch (Exception e) {
            log.error("An unexpected error occurred while getAllPrivateStps" + ExceptionUtils.getStackTrace(e) +"Logged User" + userService.getAuthenticateUser().getId());
            return null;
        }
    }


    public List<PrivateStp> getPrivateStpsByZoneTitle(String zoneTitle) {
        try {
            return privateStpRepository.findByZone_TitleIgnoreCase(zoneTitle); // Custom query
        } catch (Exception e) {
            log.error("An unexpected error occurred while getPrivateStpsByZoneTitle" + ExceptionUtils.getStackTrace(e) +"Logged User" + userService.getAuthenticateUser().getId());
            return null;
        }
    }


    public boolean save(PrivateStp privateStp) {
        try {
            privateStpRepository.save(privateStp);
            return true;
        } catch (Exception e) {
            log.error("An unexpected error occurred while save" + ExceptionUtils.getStackTrace(e) +"Logged User" + userService.getAuthenticateUser().getId());
            return false;
        }
    }

    public PrivateStp deletePrivateStp(long id) {
        try {
            return privateStpRepository.findById(id).orElse(null);
        } catch (Exception e) {
            log.error("An unexpected error occurred while deletePrivateStp" + ExceptionUtils.getStackTrace(e) +"Logged User" + userService.getAuthenticateUser().getId());
            return null;
        }
    }

    public PrivateStp getById(long id) {
        try {
            return privateStpRepository.findById(id).orElse(null);
        } catch (Exception e) {
            log.error("An unexpected error occurred while getById" + ExceptionUtils.getStackTrace(e) +"Logged User" + userService.getAuthenticateUser().getId());
            return null;
        }

    }

}

