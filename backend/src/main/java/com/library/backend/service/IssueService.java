package com.library.backend.service;

import com.library.backend.entity.Book;
import com.library.backend.entity.IssueRecord;
import com.library.backend.entity.User;
import com.library.backend.repository.BookRepository;
import com.library.backend.repository.IssueRepository;
import com.library.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public IssueRecord issueBook(Long userId, Long bookId) {

        User user = userRepository.findById(userId).orElseThrow();
        Book book = bookRepository.findById(bookId).orElseThrow();

        if (!book.getAvailability()) {
            throw new RuntimeException("Book not available");
        }

        long count = issueRepository.countByUserAndStatus(user, "ISSUED");
        if (count >= 3) {
            throw new RuntimeException("Max 3 books allowed");
        }

        book.setAvailability(false);

        IssueRecord record = new IssueRecord();
        record.setUser(user);
        record.setBook(book);
        record.setIssueDate(LocalDate.now());
        record.setStatus("ISSUED");

        bookRepository.save(book);
        return issueRepository.save(record);
    }

    @Transactional
    public IssueRecord returnBook(Long issueId) {

        IssueRecord record = issueRepository.findById(issueId).orElseThrow();

        record.setReturnDate(LocalDate.now());
        record.setStatus("RETURNED");

        Book book = record.getBook();
        book.setAvailability(true);

        bookRepository.save(book);
        return issueRepository.save(record);
    }

    @Transactional
    public IssueRecord returnBook(Long issueId, Long userId) {
        IssueRecord record = issueRepository.findById(issueId).orElseThrow();

        if (!record.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("You can only return your own books");
        }

        record.setReturnDate(LocalDate.now());
        record.setStatus("RETURNED");

        Book book = record.getBook();
        book.setAvailability(true);

        bookRepository.save(book);
        return issueRepository.save(record);
    }

    public List<IssueRecord> getBooksByUser(Long id) {
        return issueRepository.findByUser_UserIdAndStatus(id, "ISSUED");
    }

}

// package com.library.backend.service;

// import com.library.backend.entity.IssueRecord;
// import com.library.backend.repository.IssueRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.List;

// @Service
// public class IssueService {

// @Autowired
// private IssueRepository issueRepo;

// public List<IssueRecord> getBooksByUser(Long userId) {
// return issueRepo.findByUser_UserIdAndStatus(userId, "ISSUED");
// }
// }