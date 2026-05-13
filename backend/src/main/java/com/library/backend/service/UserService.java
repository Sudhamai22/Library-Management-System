// package com.library.backend.service;

// import com.library.backend.entity.User;
// import com.library.backend.repository.UserRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// @Service
// public class UserService {

//     @Autowired
//     private UserRepository userRepository;

//     // REGISTER
//     public User register(User user) {
//         return userRepository.save(user);
//     }

//     // LOGIN
//     public User login(String email, String password) {
//         User user = userRepository.findByEmail(email)
//                 .orElseThrow(() -> new RuntimeException("User not found"));

//         if (!user.getPassword().equals(password)) {
//             throw new RuntimeException("Invalid password");
//         }

//         return user;
//     }

//     // GET USER BY ID
//     public User getUser(Long id) {
//         return userRepository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("User not found"));
//     }
// }

package com.library.backend.service;

import com.library.backend.entity.User;
import com.library.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // =========================
    // REGISTER (Encrypt Password)
    // =========================
    @Transactional
    public User register(User user) {
        // Check for duplicate email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // encrypt password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("MEMBER"); // Default role

        return userRepository.save(user);
    }

    // =========================
    // REGISTER LIBRARIAN
    // =========================
    @Transactional
    public User registerLibrarian(User user) {
        // Check for duplicate email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // encrypt password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("LIBRARIAN");

        return userRepository.save(user);
    }

    // =========================
    // LOGIN (Check encrypted password)
    // =========================
    public User login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // compare encrypted password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    // =========================
    // GET USER BY ID
    // =========================
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}