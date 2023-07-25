package com.typeface.notification.handler;

import com.sun.security.auth.UserPrincipal;
import com.typeface.notification.configuration.JWTTokenUtility;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

public class UserHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        final String randomId = UUID.randomUUID().toString();
        System.out.println("User with Id"+randomId);
        return new UserPrincipal(randomId);
    }
}
