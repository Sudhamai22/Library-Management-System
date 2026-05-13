package com.library.backend.controller;

import com.library.backend.dto.ApiResponse;
import com.library.backend.dto.LoginRequest;
import com.library.backend.dto.LoginResponse;
import com.library.backend.dto.RegisterRequest;
import com.library.backend.entity.User;
import com.library.backend.security.JwtUtil;
import com.library.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        userService.register(user);
        ApiResponse<String> response = new ApiResponse<>("SUCCESS", "User registered successfully", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        User existing = userService.login(request.getEmail(), request.getPassword());
        String token = jwtUtil.generateTokenWithDetails(existing.getUserId(), existing.getEmail(), existing.getRole());

        LoginResponse loginResponse = new LoginResponse(
                existing.getUserId(),
                existing.getEmail(),
                existing.getName(),
                existing.getRole(),
                token);

        ApiResponse<LoginResponse> response = new ApiResponse<>("SUCCESS", "Login successful", loginResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register/librarian")
    public ResponseEntity<ApiResponse<String>> registerLibrarian(@Valid @RequestBody RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        userService.registerLibrarian(user);
        ApiResponse<String> response = new ApiResponse<>("SUCCESS", "Librarian registered successfully", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
