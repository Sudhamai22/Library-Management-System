package com.library.backend.controller;

import com.library.backend.dto.ApiResponse;
import com.library.backend.entity.IssueRecord;
import com.library.backend.entity.User;
import com.library.backend.security.JwtUtil;
import com.library.backend.service.IssueService;
import com.library.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private IssueService issueService;

    @Autowired
    private JwtUtil jwtUtil;

    // GET MEMBER DETAILS (only own data)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable Long id, HttpServletRequest request) {
        String token = extractToken(request);
        Long currentUserId = jwtUtil.extractUserId(token);

        if (!currentUserId.equals(id)) {
            ApiResponse<User> response = new ApiResponse<>("FORBIDDEN", "You can only view your own data", null);
            return ResponseEntity.status(403).body(response);
        }

        User user = userService.getUser(id);
        ApiResponse<User> response = new ApiResponse<>("SUCCESS", "Member details retrieved", user);
        return ResponseEntity.ok(response);
    }

    // GET BOOKS ISSUED TO MEMBER (only own books)
    @GetMapping("/{id}/books")
    public ResponseEntity<ApiResponse<List<IssueRecord>>> getUserBooks(@PathVariable Long id,
            HttpServletRequest request) {
        String token = extractToken(request);
        Long currentUserId = jwtUtil.extractUserId(token);

        if (!currentUserId.equals(id)) {
            ApiResponse<List<IssueRecord>> response = new ApiResponse<>("FORBIDDEN", "You can only view your own books",
                    null);
            return ResponseEntity.status(403).body(response);
        }

        List<IssueRecord> books = issueService.getBooksByUser(id);
        ApiResponse<List<IssueRecord>> response = new ApiResponse<>("SUCCESS", "Books retrieved", books);
        return ResponseEntity.ok(response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}