package com.tab.AttendanceApp.request;

import com.tab.AttendanceApp.enumeration.BreakType;
import lombok.Data;

@Data
public class BreakRequest {
    private Long attendanceId;
    private BreakType breakType;
}
