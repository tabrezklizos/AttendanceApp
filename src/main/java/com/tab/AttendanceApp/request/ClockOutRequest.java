package com.tab.AttendanceApp.request;

import com.tab.AttendanceApp.enumeration.BreakType;
import lombok.Data;

@Data
public class ClockOutRequest {
    private BreakType breakType; // Optional: null means final clock-out


}
