package com.tab.AttendanceApp.service;

import com.tab.AttendanceApp.entity.Attendance;
import com.tab.AttendanceApp.entity.BreakSession;
import com.tab.AttendanceApp.repository.AttendanceRepository;
import com.tab.AttendanceApp.repository.BreakSessionRepository;
import com.tab.AttendanceApp.request.BreakRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BreakService {

    private final BreakSessionRepository breakSessionRepo;
    private final AttendanceRepository attendanceRepo;

    public void startBreak(BreakRequest request) {
        BreakSession session = new BreakSession();
        session.setAttendanceId(request.getAttendanceId());
        session.setBreakType(request.getBreakType());
        session.setBreakStartTime(LocalDateTime.now());

        breakSessionRepo.save(session);
    }

    public void endBreak(BreakRequest request) {
        BreakSession session = breakSessionRepo
                .findTopByAttendanceIdAndBreakEndTimeIsNullOrderByBreakStartTimeDesc(request.getAttendanceId())
                .orElseThrow(() -> new RuntimeException("No active break found"));

        LocalDateTime now = LocalDateTime.now();
        session.setBreakEndTime(now);

        Duration breakDuration = Duration.between(session.getBreakStartTime(), now);
        session.setBreakDuration(breakDuration);

        breakSessionRepo.save(session);

        // Update total break duration in attendance
        Attendance attendance = attendanceRepo.findById(request.getAttendanceId())
                .orElseThrow(() -> new RuntimeException("Attendance not found"));

        Duration totalBreak = attendance.getTotalBreakDuration();
        attendance.setTotalBreakDuration(totalBreak.plus(breakDuration));

        attendanceRepo.save(attendance);
    }

}
