package com.tab.AttendanceApp.repository;

import com.tab.AttendanceApp.enumeration.UserRole;
import com.tab.AttendanceApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    public User findByUserRole(UserRole userRole);

    boolean existsUserByEmail(String email);

    Optional<User> findByEmail(String email);
}
