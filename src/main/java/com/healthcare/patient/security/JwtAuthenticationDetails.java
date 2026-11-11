package com.healthcare.patient.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class JwtAuthenticationDetails extends WebAuthenticationDetails {

    private final Long userId;

    public JwtAuthenticationDetails(
            HttpServletRequest request,
            Long userId) {

        super(request);
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}