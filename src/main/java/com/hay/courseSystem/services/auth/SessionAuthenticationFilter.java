package com.hay.courseSystem.services.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

import static com.hay.courseSystem.constans.Constans.ADMIN;
import static com.hay.courseSystem.constans.Constans.STUDENT;

@Component
public class SessionAuthenticationFilter extends OncePerRequestFilter {

    private final SessionManager sessionManager;
    public SessionAuthenticationFilter(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        String sessionToken = request.getHeader("Authorization");

        // Allow login requests without session check
        if (request.getRequestURI().contains("/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Validate session token for all other requests
        if (sessionToken == null || !sessionManager.isValidSession(sessionToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid Authorization Token");
            return;
        }

        String role = sessionManager.getRoleBySession(sessionToken);

        // Add role-based access control (RBAC)
        if (request.getRequestURI().contains("/api/admin/") && !ADMIN.equals(role)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("You do not have access to this resource");
            return;
        }

        if (request.getRequestURI().contains("/api/student/") && !STUDENT.equals(role)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("You do not have access to this resource");
            return;
        }

        String username = sessionManager.getSpecialKeyBySession(sessionToken);

        // Create an Authentication object
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(username, null, authorities);

        // Set the Authentication object in the Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Proceed if the session is valid
        filterChain.doFilter(request, response);
    }

}
