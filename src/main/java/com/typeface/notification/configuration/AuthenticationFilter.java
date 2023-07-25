package com.typeface.notification.configuration;

import com.typeface.notification.model.UserRequestDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static com.typeface.notification.configuration.SecurityConfig.EXCLUDE_URL;


public class AuthenticationFilter extends OncePerRequestFilter {
    private JWTTokenUtility jwtTokenUtility;

    public AuthenticationFilter(JWTTokenUtility jwtTokenUtility) {
        this.jwtTokenUtility = jwtTokenUtility;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        Optional<UserRequestDetail> userDetails = jwtTokenUtility.parseRequestHeader(request);
        if (userDetails.isPresent()) {
            Authentication authentication = jwtTokenUtility.getAuthentication(userDetails.get());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return Arrays.stream(EXCLUDE_URL).anyMatch(e -> new AntPathMatcher().match(e, request.getServletPath()));
    }

}

