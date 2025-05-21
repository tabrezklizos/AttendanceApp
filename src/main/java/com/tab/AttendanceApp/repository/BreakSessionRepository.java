package com.tab.AttendanceApp.repository;


import com.tab.AttendanceApp.entity.BreakSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BreakSessionRepository extends JpaRepository<BreakSession, Long> {
   Optional<BreakSession> findTopByAttendanceIdAndBreakEndTimeIsNullOrderByBreakStartTimeDesc(Long attendanceId);
}
