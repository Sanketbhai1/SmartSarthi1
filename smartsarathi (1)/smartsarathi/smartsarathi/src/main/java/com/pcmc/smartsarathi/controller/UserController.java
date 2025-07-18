package com.pcmc.smartsarathi.controller;


import com.pcmc.smartsarathi.dto.UserDTO;
import com.pcmc.smartsarathi.dto.UserResponseDTO;
import com.pcmc.smartsarathi.model.User;
import com.pcmc.smartsarathi.service.LoginDetailService;
import com.pcmc.smartsarathi.service.UserService;
import com.pcmc.smartsarathi.utils.ExceptionMessages;
import com.pcmc.smartsarathi.utils.ExceptionUtils;
import com.pcmc.smartsarathi.utils.RoleConstants;
import jakarta.validation.Valid;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private LoginDetailService loginDetailService;

    private static final Logger log = Logger.getLogger(UserController.class);


    @PostMapping("/add")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<String> createUser(@Valid @RequestBody UserDTO userDTO) {
        User loginUser = null;
        try {
             loginUser = userService.getAuthenticateUser();
            if (loginUser == null) {
                return ResponseEntity.badRequest().body(ExceptionMessages.SOMETHING_WENT_WRONG);
            }

            if (userDTO.getRoles() == null) {
                return ResponseEntity.badRequest().body("Role is required");
            }

            String role = userDTO.getRoles().toUpperCase(); // Ensure consistent role casing
            List<String> userRoles = RoleConstants.USER_ROLES;

            if (!userRoles.contains(role)) {
                return ResponseEntity.badRequest().body("Invalid role");
            }

            if (userDTO.getPassword() == null) {
                return ResponseEntity.badRequest().body("Password is required");
            }

            // Check if username already exists
            if (userService.findByUsername(userDTO.getUsername()) != null) {
                return ResponseEntity.badRequest().body(ExceptionMessages.USERNAME_ALREADY_EXIST);
            }

            // Create and save user
            User user = userService.userDtoToUser(userDTO);
            user.setPassword(encoder.encode(userDTO.getPassword()));
            user.setCreatedUser(loginUser);
            user.setModifiedUser(loginUser);
            user.setAddedTime(LocalDate.now());
            user.setModifiedTime(LocalDate.now());
            user.setStatus(true);

            boolean isSavedUser = userService.saveUser(user, loginUser);
            if (isSavedUser) {
                return ResponseEntity.status(201).body(ExceptionMessages.USER_SAVED);
            } else {
                return ResponseEntity.status(400).body(ExceptionMessages.USER_NOT_SAVED);
            }
        } catch (Exception e) {
            log.error("An unexpected error occurred while createUser(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id:\n "+ loginUser.getId());
            return ResponseEntity.badRequest().body(ExceptionMessages.SOMETHING_WENT_WRONG);
        }
    }


    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<String> updateUser(
            @PathVariable long id,
            @Valid @RequestBody UserDTO userDTO) {
        User loginUser = null;
        try {
             loginUser = userService.getAuthenticateUser();
            if (loginUser == null) {
                return ResponseEntity.badRequest().body(ExceptionMessages.SOMETHING_WENT_WRONG);
            }

            // Check if user exists
            User existingUser = userService.getUserById(id, loginUser);
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionMessages.USER_NOT_FOUND);
            }

            // Validate role if provided
            if (userDTO.getRoles() != null) {
                String role = userDTO.getRoles().toUpperCase();
                List<String> userRoles = RoleConstants.USER_ROLES;

                if (!userRoles.contains(role)) {
                    return ResponseEntity.badRequest().body("Invalid role");
                }
                existingUser.setRoles(role);
            }

            // Check username uniqueness if changed
            if (userDTO.getUsername() != null && !userDTO.getUsername().equals(existingUser.getUsername())) {
                if (userService.findByUsername(userDTO.getUsername()) != null) {
                    return ResponseEntity.badRequest().body(ExceptionMessages.USERNAME_ALREADY_EXIST);
                }
                existingUser.setUsername(userDTO.getUsername());
            }

            // Update other fields
            if (userDTO.getName() != null) {
                existingUser.setName(userDTO.getName());
            }

            if (userDTO.getPassword() != null) {
                existingUser.setPassword(encoder.encode(userDTO.getPassword()));
            }

            // Set audit fields
            existingUser.setModifiedUser(loginUser);
            existingUser.setModifiedTime(LocalDate.now());

            boolean isUpdated = userService.saveUser(existingUser, loginUser);
            if (isUpdated) {
                return ResponseEntity.ok(ExceptionMessages.USER_UPDATE);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionMessages.USER_NOT_UPDATE);
            }
        } catch (Exception e) {
            log.error("An unexpected error occurred while updateUser(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id:\n "+ loginUser.getId());
            return ResponseEntity.badRequest().body(ExceptionMessages.SOMETHING_WENT_WRONG);
        }
    }


    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<?> getUserById(@PathVariable long id) {
        User loginUser = null;
        try {
            loginUser = userService.getAuthenticateUser();
            if (loginUser == null) {
                return ResponseEntity.badRequest().body(ExceptionMessages.USER_NOT_FOUND);
            }
            User user = userService.getUserById(id, loginUser);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionMessages.USER_NOT_FOUND);
            }

            UserResponseDTO response = convertToDTO(user, new HashSet<>());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("An unexpected error occurred while getUserById(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id :\n "+ loginUser.getId());
            return ResponseEntity.badRequest().body(ExceptionMessages.SOMETHING_WENT_WRONG);
        }
    }


    @PutMapping("/disable/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<?> disableUser(@PathVariable long id) {
        User loginUser = null;
        try {
            loginUser = userService.getAuthenticateUser();
            if (loginUser == null) {
                return ResponseEntity.badRequest().body(ExceptionMessages.USER_NOT_FOUND);
            }
            User user = userService.deleteUser(id);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionMessages.USER_NOT_FOUND);
            }

            if (!user.isStatus()) {  // Direct boolean check
                return new ResponseEntity<>("User is already deactivated.", HttpStatus.BAD_REQUEST);
            }

            user.setStatus(false);
            user.setModifiedUser(loginUser);
            user.setModifiedTime(LocalDate.now());
            userService.saveUser(user,loginUser);
            return new ResponseEntity<>("User with ID " + id + " has been disabled successfully.", HttpStatus.OK);

        } catch (Exception e) {
            log.error("An unexpected error occurred while disableUser(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id :\n "+ loginUser.getId());
            return ResponseEntity.badRequest().body(ExceptionMessages.SOMETHING_WENT_WRONG);
        }
    }


    @GetMapping("/getAll")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public synchronized ResponseEntity<?> getAllUsers() {
        User loginUser=null;
        try {
            loginUser = userService.getAuthenticateUser();
            if (loginUser==null){
                return ResponseEntity.badRequest().body(ExceptionMessages.SOMETHING_WENT_WRONG);
            }
            List<User> users = userService.getAllUsers();
            if (users == null){
                return ResponseEntity.ok(ExceptionMessages.USER_NOT_FOUND);
            }
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("An unexpected error occurred while getAllUser(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id:\n "+ loginUser.getId());
            return ResponseEntity.badRequest().body(ExceptionMessages.SERVER_DOWN);
        }
    }




    public UserResponseDTO convertToDTO(User user, Set<Long> visited) {
        if (user == null) return null;

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setRoles(user.getRoles());
        dto.setAddedTime(user.getAddedTime());
        dto.setModifiedTime(user.getModifiedTime());
        dto.setStatus(true);

        // If already visited, avoid infinite recursion but still include shallow object
        if (user.getCreatedUser() != null) {
            if (visited.contains(user.getCreatedUser().getId())) {
                dto.setCreatedUser(shallowUser(user.getCreatedUser()));
            } else {
                visited.add(user.getCreatedUser().getId());
                dto.setCreatedUser(convertToDTO(user.getCreatedUser(), visited));
            }
        }

        if (user.getModifiedUser() != null) {
            if (visited.contains(user.getModifiedUser().getId())) {
                dto.setModifiedUser(shallowUser(user.getModifiedUser()));
            } else {
                visited.add(user.getModifiedUser().getId());
                dto.setModifiedUser(convertToDTO(user.getModifiedUser(), visited));
            }
        }

        return dto;

    }

    private UserResponseDTO shallowUser(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setRoles(user.getRoles());
        dto.setAddedTime(user.getAddedTime());
        dto.setModifiedTime(user.getModifiedTime());
        dto.setStatus(true);
        return dto;
    }

}
