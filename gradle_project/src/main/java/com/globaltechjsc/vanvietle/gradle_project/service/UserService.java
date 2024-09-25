package com.globaltechjsc.vanvietle.gradle_project.service;

import com.globaltechjsc.vanvietle.gradle_project.repository.BlogRepository;
import com.globaltechjsc.vanvietle.gradle_project.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
