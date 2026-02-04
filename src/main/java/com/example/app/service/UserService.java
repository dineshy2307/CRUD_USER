package com.example.app.service;

import com.example.app.domain.User;
import com.example.app.repository.UserRepository;
import java.util.List;
import java.util.UUID;

public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User register(String name, String email) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name required");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Invalid email");

        User user = new User(UUID.randomUUID().toString(), name, email);
        repo.save(user);
        return user;
    }

    public User findById(String id) {
        return repo.findById(id);
    }

    public boolean updateEmail(String id, String newEmail) {
        User user = repo.findById(id);
        user.setEmail(newEmail);
        repo.save(user);
        return true;
    }

    public boolean removeUser(String id) {
        repo.deleteById(id);
        return true;
    }

    public List<User> list() {
        return repo.findAll();
    }
}
