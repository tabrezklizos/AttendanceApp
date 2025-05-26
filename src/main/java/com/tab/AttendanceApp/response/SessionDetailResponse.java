package com.tab.AttendanceApp.response;

import com.tab.AttendanceApp.dto.DaySessionDTO;
import lombok.Data;

import java.util.List;
@Data
public class SessionDetailResponse {
    private Long userId;
    private List<DaySessionDTO> sessions;
}