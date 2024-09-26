package com.globaltechjsc.vanvietle.gradle_project.web.rest;

import com.globaltechjsc.vanvietle.gradle_project.service.AuthenticationService;
import com.globaltechjsc.vanvietle.gradle_project.service.dto.AuthenticationRequest;
import com.globaltechjsc.vanvietle.gradle_project.service.dto.AuthenticationResponse;
import com.globaltechjsc.vanvietle.gradle_project.service.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
public class AuthResource {

    private final Logger log = LoggerFactory.getLogger(AuthResource.class);

    private final AuthenticationService authenticationService;

    private static class AuthResourceException extends RuntimeException {
        private AuthResourceException(String message) {super(message);}
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthenticationResponse> signUp(
            @RequestBody SignUpRequest signUpRequest
    ) {
        return ResponseEntity.ok(authenticationService.signUp(signUpRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest authenticationRequest
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }
}
