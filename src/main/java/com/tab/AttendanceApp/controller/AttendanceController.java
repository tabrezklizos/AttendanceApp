package com.tab.AttendanceApp.controller;

import com.tab.AttendanceApp.dto.CheckInResponseDTO;
import com.tab.AttendanceApp.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

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
}
