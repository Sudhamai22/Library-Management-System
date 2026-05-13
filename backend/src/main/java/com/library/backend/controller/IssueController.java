package com.library.backend.controller;

import com.library.backend.entity.IssueRecord;
import com.library.backend.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/issues")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @PostMapping("/issue")
    public IssueRecord issueBook(@RequestParam Long userId,
            @RequestParam Long bookId) {
        return issueService.issueBook(userId, bookId);
    }

    @PutMapping("/return/{id}")
    public IssueRecord returnBook(@PathVariable Long id) {
        return issueService.returnBook(id);
    }
}