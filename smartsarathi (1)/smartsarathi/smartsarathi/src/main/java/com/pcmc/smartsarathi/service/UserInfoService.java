package com.pcmc.smartsarathi.service;


import com.pcmc.smartsarathi.model.User;
import com.pcmc.smartsarathi.utils.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserService userService;

    private static final Logger log = Logger.getLogger(UserInfoService.class);

    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            User user = userService.findByUsername(username);
            if (user != null) {
                // Converting UserInfo to UserDetails
                return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority(user.getRoles())));
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("An unexpected error occurred while loadUserByUsername() : " + ExceptionUtils.getStackTrace(e) +" with this username : "+ username);
            return null;
        }

    }
}

