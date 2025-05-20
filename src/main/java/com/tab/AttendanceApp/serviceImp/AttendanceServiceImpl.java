package com.tab.AttendanceApp.serviceImp;

import com.tab.AttendanceApp.dto.CheckInResponseDTO;
import com.tab.AttendanceApp.entity.Attendance;
import com.tab.AttendanceApp.enumeration.AttendanceStatus;
import com.tab.AttendanceApp.repository.AttendanceRepository;
import com.tab.AttendanceApp.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Override
    public CheckInResponseDTO checkIn(Long userId) {
        LocalDate today = LocalDate.now();

        Optional<Attendance> existingAttendance = attendanceRepository.findByUserIdAndDate(userId, today);

        if (existingAttendance.isPresent()) {
            return new CheckInResponseDTO("Already checked in", existingAttendance.get().getCheckInTime());
        }

        Attendance attendance = new Attendance();
        attendance.setUserId(userId);
        attendance.setDate(today);
        attendance.setCheckInTime(LocalTime.now());
        attendance.setStatus(AttendanceStatus.PRESENT);
        attendanceRepository.save(attendance);

        return new CheckInResponseDTO("Check-in successful", attendance.getCheckInTime());
    }

    @Override
    public String checkOut(Long userId) {
        LocalDate today = LocalDate.now();

        Attendance attendance = attendanceRepository.findByUserIdAndDate(userId, today)
                .orElseThrow(() -> new RuntimeException("Please check-in first"));

        if (attendance.getCheckOutTime() != null) {
            return "Already checked out";
        }

        attendance.setCheckOutTime(LocalTime.now());
        attendanceRepository.save(attendance);
        return "Check-out successful";
    }
}