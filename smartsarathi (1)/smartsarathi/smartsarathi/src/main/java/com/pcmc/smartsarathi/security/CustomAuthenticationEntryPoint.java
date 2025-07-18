package com.pcmc.smartsarathi.security;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String tokenError = (String) request.getAttribute("tokenError");
        String logout = (String) request.getAttribute("logout");
        String token = (String) request.getAttribute("token");

        Throwable cause = authException.getCause();

        response.setContentType("application/json");
        if ("INVALID_TOKEN".equals(tokenError)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid Token\"}");
        } else if ("INVALID_TOKEN".equals(logout)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Please log in Smart Sewerage, token is expired.\"}");
        } else if ("NOT_FOUND".equals(token)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\": \"Token not found in request. please log in in Smart Sewerage application.\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": " + authException.getMessage() + "}");
        }
    }
}
