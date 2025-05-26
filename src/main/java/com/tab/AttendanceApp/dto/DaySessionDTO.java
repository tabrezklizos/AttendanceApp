package com.tab.AttendanceApp.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class DaySessionDTO {
    private LocalDate date;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private String totalWorkDuration;
    private List<BreakDTO> breaks;
}