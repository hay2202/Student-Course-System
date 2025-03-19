package com.hay.courseSystem.services.auth;

import com.hay.courseSystem.models.SessionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class SessionManager {
    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);


    private final Map<String, SessionData> sessionStore = new HashMap<>();

    public String createSession(String specialKey, String role) {
        String sessionToken = UUID.randomUUID().toString();
        sessionStore.put(sessionToken, new SessionData(specialKey, role));
        return sessionToken;
    }

    public String getRoleBySession(String sessionToken) {
        SessionData sessionData = sessionStore.get(sessionToken);
        return sessionData != null ? sessionData.getRole() : null;
    }


    public String getSpecialKeyBySession(String sessionToken) {
        SessionData sessionData = sessionStore.get(sessionToken);
        return sessionData != null ? sessionData.getSpecialKey() : null;
    }

    public boolean isValidSession(String sessionToken) {
        logger.info("Validating session: {}", sessionToken );
        logger.debug("Active sessions: {}", sessionStore);
        return sessionStore.containsKey(sessionToken);
    }
}
