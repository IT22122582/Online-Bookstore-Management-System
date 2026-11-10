package com.example.library.service;

import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public List<User> searchUsers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllUsers();
        }
        String lowerQuery = query.toLowerCase();
        return userRepository.findAll().stream()
                .filter(u -> u.getUsername().toLowerCase().contains(lowerQuery) 
                          || u.getEmail().toLowerCase().contains(lowerQuery) 
                          || u.getId().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    public User saveUser(User user) {
        if (user.getId() == null || user.getId().trim().isEmpty()) {
            user.setId("U" + System.currentTimeMillis() % 1000000);
        }
        return userRepository.save(user);
    }

    public boolean deleteUser(String id) {
        return userRepository.deleteById(id);
    }
}
