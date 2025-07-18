package com.pcmc.smartsarathi.service;


import com.pcmc.smartsarathi.dto.UserDTO;
import com.pcmc.smartsarathi.model.User;
import com.pcmc.smartsarathi.repository.UserRepository;
import com.pcmc.smartsarathi.utils.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserService userService;

    private static final Logger log = Logger.getLogger(UserService.class);

    public User getUserById(long id, User loginUser) {
        try {
            Optional<User> user = userRepository.findById(id);
            return user.orElse(null);
        } catch (Exception e) {
            log.error("An unexpected error occurred while getUserById(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id:\n "+ loginUser.getId());
            return null;
        }
    }


    public List<User> getAllUsers() {
        try {
            List<User> userList = userRepository.findAll();
            if (userList.isEmpty()){
                return null;
            }
            return userList;
        } catch (Exception e) {
            log.error("An unexpected error occurred while getAllHospitals" + ExceptionUtils.getStackTrace(e) +"Logged User" + userService.getAuthenticateUser().getId());
            return null;
        }


    }

    public User deleteUser(long id) {
        try {
            return userRepository.findById(id).orElse(null);
        } catch (Exception e) {
            log.error("An unexpected error occurred while deleteUser" + ExceptionUtils.getStackTrace(e) +"Logged User" + userService.getAuthenticateUser().getId());
            return null;
        }
    }


    public User findByUsername(String username) {

        try {
            Optional<User> user = userRepository.findByUsername(username);
            return user.orElse(null);
        } catch (Exception e) {
            log.error("An unexpected error occurred while findByUsername" + ExceptionUtils.getStackTrace(e) +"Logged User" + userService.getAuthenticateUser().getId());
            return null;
        }
    }

    public boolean saveUser(User user, User loginUser) {
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            log.error("An unexpected error occurred while saveUser" + ExceptionUtils.getStackTrace(e) +"Logged User" + loginUser.getId());
            return false;
        }
    }


    public User getAuthenticateUser() {
        Optional<User> user = Optional.empty();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            user = userRepository.findByUsername(authentication.getName());
            return user.orElse(null);
        } catch (Exception e) {
            log.error("An unexpected error occurred while findByUsername" + ExceptionUtils.getStackTrace(e) +"Logged User" + userService.getAuthenticateUser().getId());
            return null;
        }

    }

    public User userDtoToUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setName(userDTO.getName());
        user.setRoles(userDTO.getRoles().toUpperCase());
        return user;
    }
}

