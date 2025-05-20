package com.tab.AttendanceApp.repository;

import com.tab.AttendanceApp.enumeration.UserRole;
import com.tab.AttendanceApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    public User findByUserRole(UserRole userRole);
}
