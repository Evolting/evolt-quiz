package com.evolting.authservice.services;

import com.evolting.authservice.entities.User;

public interface UserService {
    User findByUsername(String username);
    String register(User user);
}
