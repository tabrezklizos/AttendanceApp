package com.tab.AttendanceApp.entity;

import com.tab.AttendanceApp.enumeration.AttendanceStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "attendance")
@Data
public class Attendance{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private LocalDate date;

    private LocalTime checkInTime;

    private LocalTime checkOutTime;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    // Duration fields stored in seconds
    @Column(name = "total_break_duration_seconds")
    private Long totalBreakDurationSeconds = 0L;

    @Column(name = "total_work_duration_seconds")
    private Long totalWorkDurationSeconds = 0L;

    // Transient getter/setter for Duration object usage in code
    @Transient
    public Duration getTotalBreakDuration() {
        return Duration.ofSeconds(totalBreakDurationSeconds == null ? 0 : totalBreakDurationSeconds);
    }

    @Transient
    public void setTotalBreakDuration(Duration duration) {
        this.totalBreakDurationSeconds = duration != null ? duration.getSeconds() : 0L;
    }

    @Transient
    public Duration getTotalWorkDuration() {
        return Duration.ofSeconds(totalWorkDurationSeconds == null ? 0 : totalWorkDurationSeconds);
    }

    @Transient
    public void setTotalWorkDuration(Duration duration) {
        this.totalWorkDurationSeconds = duration != null ? duration.getSeconds() : 0L;
    }
}