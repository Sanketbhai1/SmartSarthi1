package com.pcmc.smartsarathi.service;


import com.pcmc.smartsarathi.model.STPForm;
import com.pcmc.smartsarathi.model.Zone;
import com.pcmc.smartsarathi.repository.STPFormRepository;
import com.pcmc.smartsarathi.utils.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class STPFormService {

    @Autowired
    STPFormRepository stpFormRepository;

    @Autowired
    private UserService userService;

    private static final Logger log = Logger.getLogger(STPFormService.class);


    private static boolean status;

    public boolean saveSTPForm(STPForm stpForm) {
        try {
            stpFormRepository.save(stpForm);
            return true;
        } catch (Exception e) {
            log.error("An unexpected error occurred while saveSTPForm" + ExceptionUtils.getStackTrace(e) +"Logged User" + userService.getAuthenticateUser().getId());
            return false;
        }
    }

    public List<STPForm> getSTPFormsBySocietyName(String societyName)
    {
        try {
            return stpFormRepository.findBySocietyNameContainingIgnoreCase(societyName);
        } catch (Exception e) {
            log.error("An unexpected error occurred while getSTPFormsBySocietyName" + ExceptionUtils.getStackTrace(e) +"Logged User" + userService.getAuthenticateUser().getId());
            return null;
        }
    }

    public STPForm getSTPFormById(long id) {
        try {
            return stpFormRepository.findById(id).orElse(null);
        } catch (Exception e) {
            log.error("An unexpected error occurred while getSTPFormById" + ExceptionUtils.getStackTrace(e) +"Logged User" + userService.getAuthenticateUser().getId());
            return null;
        }
    }

    public List<STPForm> findAllSTPForm() {
              try
              {
                  return stpFormRepository.findAll();
              } catch (Exception e) {
                  log.error("An unexpected error occurred while findAllSTPForm" + ExceptionUtils.getStackTrace(e) +"Logged User" + userService.getAuthenticateUser().getId());
                  return null;
              }
    }

    public STPForm deleteStpForm(long id) {
        try {
            return stpFormRepository.findById(id).orElse(null);
        } catch (Exception e) {
            log.error("An unexpected error occurred while deleteStpForm" + ExceptionUtils.getStackTrace(e) +"Logged User" + userService.getAuthenticateUser().getId());
            return null;
        }
    }

    public List<STPForm> findActiveStpForm() {
        status = true;
        try {
            List<STPForm> byStatus = stpFormRepository.findByStatus(status);
            return byStatus;
        } catch (Exception e) {
            log.error("An unexpected error occurred while findActiveStpForm" + ExceptionUtils.getStackTrace(e) +"Logged User" + userService.getAuthenticateUser().getId());
            return null;
        }
    }


}
