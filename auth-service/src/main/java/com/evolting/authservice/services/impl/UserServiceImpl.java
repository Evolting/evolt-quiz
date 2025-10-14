package com.evolting.authservice.services.impl;

import com.evolting.authservice.entities.Role;
import com.evolting.authservice.entities.User;
import com.evolting.authservice.repositories.RoleRepository;
import com.evolting.authservice.repositories.UserRepository;
import com.evolting.authservice.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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

    @Cacheable(value = "users", key = "#username")
    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user;
    }

    @Override
    public User register(User user) {
        if (findByUsername(user.getUsername()) != null) {
            return null;
        }
        user.setPassword(encoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        Random random = new Random();
        if(random.nextInt() % 2 == 0) {
            roles.add(roleRepository.findById(2).get());
        }
        else roles.add(roleRepository.findById(1).get());
        user.setRoles(roles);
        User savedUser = userRepository.save(user);
        return savedUser;
    }
}
