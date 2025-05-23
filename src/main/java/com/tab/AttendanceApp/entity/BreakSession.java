package com.tab.AttendanceApp.entity;

import com.tab.AttendanceApp.enumeration.BreakType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "break_session")
@Data
public class BreakSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long attendanceId;

    @Enumerated(EnumType.STRING)
    private BreakType breakType;

    private LocalDateTime breakStartTime;

    private LocalDateTime breakEndTime;

    private Duration breakDuration;
}
