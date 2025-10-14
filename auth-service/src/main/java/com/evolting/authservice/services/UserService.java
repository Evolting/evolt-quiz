package com.evolting.authservice.services;

import com.evolting.authservice.entities.User;

public interface UserService {
    User findByUsername(String username);
    User register(User user);
}
