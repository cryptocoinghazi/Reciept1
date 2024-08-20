package com.reciept.Reciept;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private  UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Transactional
    public void addUser(String username) {
        // Check if the username already exists
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            // Create a new User instance and save it
            User newUser = new User(username);
            userRepository.save(newUser);
        }
    }

    // Delete a user by username
    @Transactional
    public boolean deleteUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            userRepository.deleteByUsername(username);
            return true;
        }
        return false;
    }

    // Load all users
    public List<User> loadUsers() {
        return userRepository.findAll();
    }

    // Find a user by username
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
