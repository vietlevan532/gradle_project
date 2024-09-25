package com.globaltechjsc.vanvietle.gradle_project.service;

import com.globaltechjsc.vanvietle.gradle_project.config.jwt.JwtService;
import com.globaltechjsc.vanvietle.gradle_project.domain.Role;
import com.globaltechjsc.vanvietle.gradle_project.domain.User;
import com.globaltechjsc.vanvietle.gradle_project.repository.UserRepository;
import com.globaltechjsc.vanvietle.gradle_project.service.dto.AuthenticationRequest;
import com.globaltechjsc.vanvietle.gradle_project.service.dto.AuthenticationResponse;
import com.globaltechjsc.vanvietle.gradle_project.service.dto.SignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse signUp(SignUpRequest request) {
        if (userRepository.findByUsername(request.getEmail().substring(0, request.getEmail().indexOf("@"))).isPresent()) {
            return AuthenticationResponse.builder()
                    .token("Email " + request.getEmail() + " has been used, please enter another email!")
                    .build();
        } else if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return AuthenticationResponse.builder()
                    .token("Email " + request.getEmail() + " has been used, please enter another email!")
                    .build();
        } else if (userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            return AuthenticationResponse.builder()
                    .token("Phone " + request.getPhoneNumber() + " has been used!")
                    .build();
        } else {
            var user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .username(request.getEmail().substring(0, request.getEmail().indexOf("@")))
                    .phoneNumber(request.getPhoneNumber())
                    .address(request.getAddress())
                    .gender(request.getGender())
                    .birthOfDate(request.getDateOfBirth())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .image("https://thumbs.dreamstime.com/b/default-avatar-profile-icon-vector-social-media-user-image-182145777.jpg")
                    .status(true)
                    .role(Role.ROLE_USER)
                    .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("H:mm dd/MM/yyyy")))
                    .updatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("H:mm dd/MM/yyyy")))
                    .build();
            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user, "", false);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getLogin())
                .orElseGet(() -> userRepository.findByPhoneNumber(request.getLogin())
                        .orElseGet(() -> userRepository.findByUsername(request.getLogin())
                                .orElseThrow(() -> new UsernameNotFoundException("Invalid login information or password"))));
        var jwtToken = jwtService.generateToken(user, request.getLogin(), request.getRememberMe());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
