package com.tab.AttendanceApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class CheckInResponseDTO {
    private String message;
    private LocalTime checkInTime;


}