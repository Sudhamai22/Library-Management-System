package com.library.backend.controller;

import com.library.backend.entity.IssueRecord;
import com.library.backend.entity.User;
import com.library.backend.service.IssueService;
import com.library.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private IssueService issueService;

    // GET USER DETAILS
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    // GET BOOKS ISSUED TO USER
    @GetMapping("/{id}/books")
    public List<IssueRecord> getUserBooks(@PathVariable Long id) {
        return issueService.getBooksByUser(id);
    }
}