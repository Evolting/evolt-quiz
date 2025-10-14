package com.evolting.authservice.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/hello-user")
    public String helloUser() {
        return "Hello User!";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/hello-admin")
    public String helloAdmin() {
        return "Hello Admin!";
    }
}
