package com.example.app.repository;

import com.example.app.domain.User;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class UserRepository {
    private final Map<String, User> storage;
    private final Path filePath;

    public UserRepository(Path filePath) {
        storage = new HashMap<>();
        this.filePath = filePath;
        loadFromDisk();
    }

    public void save(User user) {
        storage.put(user.getId(), user);
        persistToDisk();
    }

    public User findById(String id) {
        return storage.getOrDefault(id, null);
    }

    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    public boolean deleteById(String id) {
        return storage.remove(id) != null;
    }

    private void loadFromDisk() {

        try (BufferedReader reader = Files.newBufferedReader(this.filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                // CSV: id,name,email
                String[] parts = line.split(",", -1);
                if (parts.length != 3) continue;

                String id = parts[0];
                String name = parts[1];
                String email = parts[2];

                storage.put(id, new User(id, name, email));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load users from disk: " + filePath, e);
        }
    }

    private void persistToDisk() {
        try {
            Files.createDirectories(filePath.getParent());

            try (BufferedWriter writer = Files.newBufferedWriter(
                    filePath,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            )) {
                for (User u : storage.values()) {
                    // Basic CSV. (No commas allowed in name/email for now.)
                    writer.write(u.getId() + "," + u.getName() + "," + u.getEmail());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to persist users to disk: " + filePath, e);
        }
    }
}