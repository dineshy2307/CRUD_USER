package com.example.app.controller;

import com.example.app.domain.User;
import com.example.app.service.UserService;
import java.util.List;

public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    public User registerUser(String name, String email) {
        return service.register(name, email);
    }

    public User getUserById(String id) {
        return service.findById(id);
    }

    public boolean updateUserEmail(String id, String newEmail) {
        return service.updateEmail(id, newEmail);
    }

    public void removeUser(String id) {
        service.removeUser(id);
    }

    public List<User> listUsers() {
        return service.list();
    }
}
