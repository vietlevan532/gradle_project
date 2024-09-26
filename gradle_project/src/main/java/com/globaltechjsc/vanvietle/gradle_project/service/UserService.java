package com.globaltechjsc.vanvietle.gradle_project.service;

import com.globaltechjsc.vanvietle.gradle_project.domain.User;
import com.globaltechjsc.vanvietle.gradle_project.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserLogin(String login) {
        return userRepository.findByEmail(login)
                .orElseGet(() -> userRepository.findByPhoneNumber(login)
                        .orElseGet(() -> userRepository.findByUsername(login)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found!"))));
    }
}
