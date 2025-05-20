package com.tab.AttendanceApp.service;

import com.tab.AttendanceApp.dto.CheckInResponseDTO;

public interface AttendanceService {
    CheckInResponseDTO checkIn(Long userId);
    String checkOut(Long userId);
}
