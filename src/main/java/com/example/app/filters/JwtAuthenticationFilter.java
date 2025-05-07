package com.example.app.filters;

import com.example.app.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Component // This annotation makes the filter a Spring bean specific to the application context
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // If there's no token or it doesn't start with "Bearer ", continue with filtering
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the token (remove the "Bearer " prefix)
        String token = authHeader.substring(7);

        // Validate the token
        if (!jwtService.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // If the token is valid, extract the username and roles
        String username = jwtService.extractUsername(token);
        String rolesString = jwtService.extractRoles(token);

        // Convert the roles string to a list of Spring Security authorities
        Collection<GrantedAuthority> authorities = extractAuthorities(rolesString);

        // Create an Authentication object with user information
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(username, null, authorities);

        // Set Authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Continue with the rest of the filters
        filterChain.doFilter(request, response);
    }

    private Collection<GrantedAuthority> extractAuthorities(String rolesString) {
        // Process the roles string. Example: "[ROLE_ADMIN, ROLE_USER]"
        if (rolesString == null || rolesString.isEmpty()) {
            return Collections.emptyList();
        }

        // Remove brackets and split by commas
        String roles = rolesString.replace("[", "").replace("]", "");
        String[] roleArray = roles.split(",");

        return Arrays.stream(roleArray)
                .map(String::trim) // Remove spaces
                .map(SimpleGrantedAuthority::new) // We'll have roles in the correct format for Spring Security to recognize them
                .collect(Collectors.toList());
    }
}