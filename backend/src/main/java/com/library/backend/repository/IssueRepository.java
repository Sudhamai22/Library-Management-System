package com.library.backend.repository;

import com.library.backend.entity.IssueRecord;
import com.library.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<IssueRecord, Long> {

    long countByUserAndStatus(User user, String status);

    List<IssueRecord> findByUser_UserIdAndStatus(Long userId, String status);
}

// package com.library.backend.repository;

// import com.library.backend.entity.IssueRecord;
// import org.springframework.data.jpa.repository.JpaRepository;

// import java.util.List;

// public interface IssueRepository extends JpaRepository<IssueRecord, Long> {

// List<IssueRecord> findByUser_UserIdAndStatus(Long userId, String status);
// }