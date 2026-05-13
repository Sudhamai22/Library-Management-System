package com.library.backend.controller;

import com.library.backend.dto.ApiResponse;
import com.library.backend.entity.IssueRecord;
import com.library.backend.security.JwtUtil;
import com.library.backend.service.IssueService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/issues")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/issue")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<ApiResponse<IssueRecord>> issueBook(@RequestParam Long bookId, HttpServletRequest request) {
        String token = extractToken(request);
        Long userId = jwtUtil.extractUserId(token);

        try {
            IssueRecord record = issueService.issueBook(userId, bookId);
            ApiResponse<IssueRecord> response = new ApiResponse<>("SUCCESS", "Book issued successfully", record);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            ApiResponse<IssueRecord> response = new ApiResponse<>("ERROR", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/return/{id}")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<ApiResponse<IssueRecord>> returnBook(@PathVariable Long id, HttpServletRequest request) {
        String token = extractToken(request);
        Long userId = jwtUtil.extractUserId(token);

        try {
            IssueRecord record = issueService.returnBook(id, userId);
            ApiResponse<IssueRecord> response = new ApiResponse<>("SUCCESS", "Book returned successfully", record);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<IssueRecord> response = new ApiResponse<>("ERROR", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}