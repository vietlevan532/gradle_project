package com.globaltechjsc.vanvietle.gradle_project.config.jwt;

import com.globaltechjsc.vanvietle.gradle_project.domain.User;
import com.globaltechjsc.vanvietle.gradle_project.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String login;

        // todo: Check if Authorization header contains Bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // todo: Extract JWT from header
        jwt = authHeader.substring(7);

        // todo: Extract login(username, email, or phoneNumber) from JWT token
        login = jwtService.extractLogin(jwt);

        // todo: Check login != null and no authentication in SecurityContext
        if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // todo: Load user information from userDetailsService
            User userDetails = userRepository.findByEmail(login)
                    .orElseGet(() -> userRepository.findByPhoneNumber(login)
                            .orElseGet(() -> userRepository.findByUsername(login)
                                    .orElseThrow(() -> new UsernameNotFoundException("User not found!"))));
            // todo: Check the valid token
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // todo: Create authentication object UsernamePasswordAuthenticationToken
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, // (Object principal) ís a userDetails
                        null, // (Object credentials) is password but don't provide password to be save so we let's make "null"
                        userDetails.getAuthorities() // (Collection<? extends GrantedAuthority> authorities) List of user authorities
                );
                // todo: Set authentication details
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // todo: Set authentication object into SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // todo: Continue the filterChain
        filterChain.doFilter(request, response);
    }
}
