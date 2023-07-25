package com.typeface.notification.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.typeface.notification.configuration.JWTTokenUtility;
import com.typeface.notification.entity.User;
import com.typeface.notification.model.LoginResponse;
import com.typeface.notification.model.UserRequestDetail;
import com.typeface.notification.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class LoginService {

    public static final String CLIENT_ID = "";
    @Autowired
    private JWTTokenUtility jwtTokenUtility;
    @Autowired
    private IUserRepository userRepository;

    public LoginResponse authenticate(String token) {
        User user = mapUserFromToken(token);
        String data =  getToken(user);
        LoginResponse response = new LoginResponse();
        response.data = data;
        return response;
    }
    public User mapUserFromToken(String token) {
        GoogleIdToken idToken = validateToken(token);
        GoogleIdToken.Payload payload = idToken.getPayload();
        User userEntity = new User();
        userEntity.setEmail(payload.getEmail());
        return userEntity;
    }

    private String getToken(User user) {
        User validUser = this.userRepository.findByEmail(user.getEmail());
        UserRequestDetail userReqDetails = UserRequestDetail.builder()
                .userID(validUser.getId())
                .email(validUser.getEmail())
                .build();
        return jwtTokenUtility.generateToken(userReqDetails);
    }
    private GoogleIdToken validateToken(String token) {
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory mJFactory = new GsonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, mJFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();
        try {
            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                return idToken;
            }
            throw new AccessDeniedException("Invalid Token");
        } catch (IllegalArgumentException | GeneralSecurityException | IOException ex) {
            throw new AccessDeniedException("Token Expired");
        }
    }
}
