package com.tab.AttendanceApp.controller;

import com.tab.AttendanceApp.dto.CheckInResponseDTO;
import com.tab.AttendanceApp.entity.Attendance;
import com.tab.AttendanceApp.enumeration.BreakType;
import com.tab.AttendanceApp.request.ClockOutRequest;
import com.tab.AttendanceApp.response.SessionDetailResponse;
import com.tab.AttendanceApp.service.AttendanceService;
import com.tab.AttendanceApp.serviceImp.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/clock-in")
    public ResponseEntity<?> clockIn(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtService.extractUserId(token);
        CheckInResponseDTO dto = attendanceService.clockIn(userId);
        return ResponseEntity.ok(dto);
    }
    @PostMapping("/clock-out")
    public ResponseEntity<?> clockOut(
            HttpServletRequest request,
            @RequestBody(required = false) ClockOutRequest body
    ) {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtService.extractUserId(token);

        BreakType breakType = (body != null) ? body.getBreakType() : null;

        String message = attendanceService.clockOut(userId, breakType);
        return ResponseEntity.ok(message);
    }
    @PostMapping("/check-in/{userId}")
    public ResponseEntity<?> checkIn(@PathVariable Long userId) {
        CheckInResponseDTO checkInResponseDTO = attendanceService.checkIn(userId);
        return  ResponseEntity.ok(checkInResponseDTO);
    }
    @PostMapping("/check-out/{userId}")
    public ResponseEntity<String> checkOut(@PathVariable Long userId) {
        String message = attendanceService.checkOut(userId);
        return ResponseEntity.ok(message);
    }
    @GetMapping("/session-details")
    public ResponseEntity<SessionDetailResponse> getSessionDetails(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtService.extractUserId(token);
        SessionDetailResponse response = attendanceService.getSessionDetails(userId);
        return ResponseEntity.ok(response);
    }
}
