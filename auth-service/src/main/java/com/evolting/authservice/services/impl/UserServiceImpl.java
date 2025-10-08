package com.evolting.authservice.services.impl;

import com.evolting.authservice.entities.User;
import com.evolting.authservice.repositories.RoleRepository;
import com.evolting.authservice.repositories.UserRepository;
import com.evolting.authservice.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        log.info("Finding user by username: {} -> {}", username, user.toString());
        return user;
    }

    @Override
    public String register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(roleRepository.findById(1).get());
        userRepository.save(user);
        return "Register done";
    }
}
