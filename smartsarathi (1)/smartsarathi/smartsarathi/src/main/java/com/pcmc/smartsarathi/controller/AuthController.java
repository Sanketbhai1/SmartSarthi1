package com.pcmc.smartsarathi.controller;


import com.pcmc.smartsarathi.dto.LoginResponse;
import com.pcmc.smartsarathi.model.AuthRequest;
import com.pcmc.smartsarathi.model.LoginDetails;
import com.pcmc.smartsarathi.model.User;
import com.pcmc.smartsarathi.service.JwtService;
import com.pcmc.smartsarathi.service.LoginDetailService;
import com.pcmc.smartsarathi.service.UserService;
import com.pcmc.smartsarathi.utils.EncryptionUtil;
import com.pcmc.smartsarathi.utils.ExceptionMessages;
import com.pcmc.smartsarathi.utils.ExceptionUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private LoginDetailService loginDetailService;

    @Autowired
    private EncryptionUtil encryptionUtil;

    private static final Logger log = Logger.getLogger(AuthController.class);


    @PostMapping("/login")
    public synchronized ResponseEntity<?> authenticateAndGetToken(
            @Valid @RequestBody AuthRequest authRequest,
            HttpServletRequest request) {
        User loginUser = null;
        String clientIp = getClientIp(request, authRequest);
        try {
            // Retrieve the user by username
            loginUser = userService.findByUsername(authRequest.getUsername());

            if (loginUser == null) {
                return ResponseEntity.badRequest().body(ExceptionMessages.INVALID_USERNAME_PASSWORD);
            }

            // Check if the user is active or not
            if (!loginUser.isStatus()) {
                return ResponseEntity.badRequest().body(ExceptionMessages.USER_IS_NOT_ACTIVE);
            }


//            1 . new UsernamePasswordAuthenticationToken() = This creates an instance of UsernamePasswordAuthenticationToken, which is a standard Spring Security class representing a login attempt with a username and password.
//            2 . authenticationManager.authenticate(...) = 1) Delegates more AuthenticationProviders (like DaoAuthenticationProvider).
//                                                          2) The provider attempts to load the user from the database (via UserDetailsService) using the username.
//                                                          3) If a user is found, the provider then checks the password using the configured PasswordEncoder.
//                                                          4) If everything matches, it returns a fully authenticated Authentication object (with roles, authorities, etc.).

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                // Create session manually if not exists
                HttpSession session = request.getSession(true); //Ensures session is created
                LoginDetails loginDetails = loginDetailService.findByUsername(loginUser.getUsername());


                if (loginDetails == null) {
                    LoginDetails newLoginDetails = new LoginDetails();

//                    String token = encryptionUtil.encrypt(jwtService.generateToken(authRequest.getUsername(), user));
//                    newLoginDetails.setToken(token);
                    newLoginDetails.setIpAddress(clientIp);
                    newLoginDetails.setCreated(loginUser);
                    newLoginDetails.setUsername(loginUser.getUsername());
                    newLoginDetails.setLoginAt(LocalDateTime.now());

                    boolean isSavedLoginDetails = loginDetailService.saveLoginDetails(newLoginDetails);
                    if (!isSavedLoginDetails) {
                        return ResponseEntity.badRequest().body(ExceptionMessages.SOMETHING_WENT_WRONG);
                    }
                }
                String token = encryptionUtil.encrypt(jwtService.generateToken(authRequest.getUsername(), loginUser));
                LoginResponse response = new LoginResponse();
                response.setId(loginUser.getId());
                response.setRoles(loginUser.getRoles());
                response.setToken(token);
                session.setAttribute("jwtToken", token);
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.badRequest().body(ExceptionMessages.AUTHENTICATION_FAILED);
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(ExceptionMessages.INVALID_USERNAME_PASSWORD);
        } catch (Exception e) {
            log.error("An unexpected error occurred while logIn(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id :\n "+ loginUser.getId());
            return ResponseEntity.badRequest().body(ExceptionMessages.SERVER_DOWN);
        }
    }

    @PostMapping("/logout")
    public synchronized ResponseEntity<?> logout(HttpServletRequest request) {
        User user = null;
        try {

            HttpSession session = request.getSession(false); // Don't create a new session if it doesn't exist
            String encryptedToken = null;
            if (session != null) {
                encryptedToken = session.getAttribute("jwtToken").toString();
                session.removeAttribute("jwtToken");
                session.invalidate();

            }

            // 2. Check if JWT token is present in the Authorization header
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (encryptedToken == null && authHeader != null && authHeader.startsWith("Bearer ")) {
                encryptedToken = authHeader.substring(7);
            }

            if (encryptedToken == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token Not Found in Request. please login.");
            }

            String decryptedToken = encryptionUtil.decrypt(encryptedToken);

            String username = jwtService.extractUsername(decryptedToken);
            LoginDetails loginDetails = loginDetailService.findByUsername(username);

            if (loginDetails == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionMessages.LOGIN_DETAILS_NOT_FOUND);
            }
            // Retrieve the user by username
            user = userService.findByUsername(username);

            if (user == null) {
                return ResponseEntity.badRequest().body(ExceptionMessages.SOMETHING_WENT_WRONG);
            }

            loginDetailService.delete(loginDetails);


            return ResponseEntity.ok(ExceptionMessages.LOGGED_OUT_SUCCESS);
        } catch (Exception e) {
            log.error("An unexpected error occurred while logOut(): \n"+ ExceptionUtils.getStackTrace(e) +"\n"+"Logged User Id :\n "+ user.getId());
            return ResponseEntity.badRequest().body(ExceptionMessages.SERVER_DOWN);
        }

    }


    private String getClientIp(HttpServletRequest request, AuthRequest authRequest) {
        try {
            String ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Real-IP");
            }
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            return ip;
        } catch (Exception e) {
            log.error("An unexpected error occurred while getClientIp() : \n" + ExceptionUtils.getStackTrace(e) + "\n" + " Username : \n " + authRequest.getUsername());
            return "";
        }

    }

}