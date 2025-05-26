package com.tab.AttendanceApp.service;

import com.tab.AttendanceApp.dto.CheckInResponseDTO;
import com.tab.AttendanceApp.entity.Attendance;
import com.tab.AttendanceApp.enumeration.BreakType;
import com.tab.AttendanceApp.response.SessionDetailResponse;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    CheckInResponseDTO checkIn(Long userId);
    String checkOut(Long userId);

    CheckInResponseDTO clockIn(Long userId);
    String clockOut(Long userId, BreakType breakType);

    //List<Attendance> findByUserIdAndDateBetween(Long userId, LocalDate twoDaysAgo, LocalDate today);

    SessionDetailResponse getSessionDetails(Long userId);
}
