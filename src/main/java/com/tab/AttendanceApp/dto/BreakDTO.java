package com.tab.AttendanceApp.dto;

import com.tab.AttendanceApp.enumeration.BreakType;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class BreakDTO {
    private BreakType type;
    private LocalDateTime start;
    private LocalDateTime end;
    private String duration;
}