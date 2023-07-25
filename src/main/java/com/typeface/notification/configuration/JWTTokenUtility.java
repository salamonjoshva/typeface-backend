package com.typeface.notification.configuration;

import com.typeface.notification.model.AbstractUserRequestDetails;
import com.typeface.notification.model.UserRequestDetail;
import com.typeface.notification.service.UserManagementService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class JWTTokenUtility {

    public static final String SECRET_KEY = "sdfjaks43saiodawiorWI";
    private UserManagementService userManagementService;

    @Autowired
    public JWTTokenUtility(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    private static AbstractUserRequestDetails getSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AccessDeniedException("Authentication is empty");
        }
        if (authentication.getPrincipal() == null) {
            throw new AccessDeniedException("User request is empty");
        }
        return authentication.getPrincipal() instanceof AbstractUserRequestDetails ? (AbstractUserRequestDetails) authentication.getPrincipal() : null;
    }

    public static Long getUserID() { return getSecurityContext().getUserID(); }

    public static String getUserEmail() {
        return Optional.ofNullable(getSecurityContext()).map(AbstractUserRequestDetails::getEmail).orElse(null);
    }

    public static String getUserName() {
        return Optional.ofNullable(getSecurityContext()).map(AbstractUserRequestDetails::getUsername).orElse(null);
    }

    private String getHeader(HttpServletRequest request) {

        String header = request.getHeader("Authorization");
        if (header == null) {
            throw  new AccessDeniedException("Token required");
        }
        if (!header.startsWith("Bearer ")) {
            throw new AccessDeniedException("Invalid token");
        }
        return header;
    }

    public Authentication getAuthentication(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

    }

    //Extracting token from Header
    public Optional<UserRequestDetail> parseRequestHeader(HttpServletRequest request) {

        String header = getHeader(request);

        String jwtToken = header.substring("Bearer ".length());
        Claims claims = getClaimsFromToken(jwtToken);

        UserRequestDetail requestDetails = UserRequestDetail.builder()
                .userID(claims.get("user_id", Long.class))
                .email((String) claims.get("email"))
                .build();

        if (!userManagementService.validate(requestDetails)) {
            throw new AccessDeniedException("Invalid Token");
        }

        return Optional.ofNullable(requestDetails);
    }

    //generate token for user
    public String generateToken(UserRequestDetail userDetails) {
        return generateToken(getClaims(userDetails), SECRET_KEY);
    }

    private Map<String, Object> getClaims(UserRequestDetail userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userDetails.getEmail());
        claims.put("user_id", userDetails.getUserID());
        return claims;
    }

    public static String generateToken(Map<String, Object> claims, String secretKey) {
        return Jwts.builder().setClaims(claims).setSubject("user").setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 3600))
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

    public static String getSecretValue(String secret) {
        if (secret != null && !secret.isEmpty()) {
            byte[] key = Objects.requireNonNull(secret).getBytes();
            return Base64.getEncoder().encodeToString(key);
        }
        return null;
    }

    public static Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException ex) {
            throw new AccessDeniedException("Token Expired");
        } catch (SignatureException e) {
            throw e;
        } catch (Exception e) {
            throw new AccessDeniedException("Invalid Token");
        }
    }
}
