    package com.tab.AttendanceApp.serviceImp;

    import com.tab.AttendanceApp.dto.BreakDTO;
    import com.tab.AttendanceApp.dto.CheckInResponseDTO;
    import com.tab.AttendanceApp.dto.DaySessionDTO;
    import com.tab.AttendanceApp.entity.Attendance;
    import com.tab.AttendanceApp.entity.BreakSession;
    import com.tab.AttendanceApp.enumeration.AttendanceStatus;
    import com.tab.AttendanceApp.enumeration.BreakType;
    import com.tab.AttendanceApp.repository.AttendanceRepository;
    import com.tab.AttendanceApp.repository.BreakSessionRepository;
    import com.tab.AttendanceApp.response.SessionDetailResponse;
    import com.tab.AttendanceApp.service.AttendanceService;
    import com.tab.AttendanceApp.service.BreakService;
    import lombok.extern.slf4j.Slf4j;
    import org.modelmapper.ModelMapper;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.time.Duration;
    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.time.LocalTime;
    import java.util.List;
    import java.util.Map;
    import java.util.Optional;
    import java.util.stream.Collectors;

    @Slf4j
    @Service
    public class AttendanceServiceImpl implements AttendanceService {

        @Autowired
        private AttendanceRepository attendanceRepository;
        @Autowired
        private BreakService breakService;
        @Autowired
        private  BreakSessionRepository breakSessionRepo;
        @Autowired
        private ModelMapper modelMapper;

        @Override
        public CheckInResponseDTO clockIn(Long userId) {
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();

            Optional<Attendance> optional = attendanceRepository.findByUserIdAndDate(userId, today);
            Attendance attendance;

            if (optional.isEmpty()) {
                // First clock-in of the day
                attendance = new Attendance();
                attendance.setUserId(userId);
                attendance.setDate(today);
                attendance.setCheckInTime(now);
                attendance.setTotalBreakDuration(Duration.ZERO);
                attendance.setTotalWorkDuration(Duration.ZERO);
                attendance.setStatus(AttendanceStatus.PRESENT);
                attendanceRepository.save(attendance);

                return new CheckInResponseDTO("Day started (Check-in)", now);
            }

            attendance = optional.get();

            if (attendance.getCheckOutTime() != null) {
                return new CheckInResponseDTO("Already checked out for the day", attendance.getCheckOutTime());
            }

            // Resume from break
            BreakSession latestBreak = breakSessionRepo.findTopByAttendanceIdOrderByBreakStartTimeDesc(attendance.getId())
                    .orElseThrow(() -> new RuntimeException("No break found to end"));

            if (latestBreak.getBreakEndTime() == null) {
                latestBreak.setBreakEndTime(LocalDateTime.now());
                Duration breakDuration = Duration.between(latestBreak.getBreakStartTime(), latestBreak.getBreakEndTime());
                latestBreak.setBreakDuration(breakDuration);
                breakSessionRepo.save(latestBreak);

                // Update total break duration in Attendance
                Duration current = attendance.getTotalBreakDuration();
                attendance.setTotalBreakDuration(current.plus(breakDuration));
                attendanceRepository.save(attendance);

                return new CheckInResponseDTO("Resumed work after break", now);
            }

            return new CheckInResponseDTO("Already working", now);
        }

        @Override
        public String clockOut(Long userId, BreakType breakType) {
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();

            Attendance attendance = attendanceRepository.findByUserIdAndDate(userId, today)
                    .orElseThrow(() -> new RuntimeException("No check-in found"));

            if (attendance.getCheckOutTime() != null) {
                return "Already checked out";
            }

            if (breakType == null) {
                // Final clock-out of the day
                LocalTime checkOutTime = now;
                attendance.setCheckOutTime(checkOutTime);

                Duration fullSession = Duration.between(attendance.getCheckInTime(), checkOutTime);
                List<BreakSession> breaks = breakService.getBreaksByAttendanceId(attendance.getId());
                Duration effectiveBreaks = breakService.calculateEffectiveBreaks(breaks);

                Duration extraBreak = effectiveBreaks.minus(Duration.ofHours(1)).isNegative()
                        ? Duration.ZERO
                        : effectiveBreaks.minus(Duration.ofHours(1));

                Duration netWorkDuration = fullSession.minus(attendance.getTotalBreakDuration()).plus(extraBreak);
                attendance.setTotalWorkDuration(netWorkDuration);

                attendanceRepository.save(attendance);

                return "Final check-out. Total worked: " + netWorkDuration;
            }

            // Start a break
            BreakSession breakSession = new BreakSession();
            breakSession.setAttendanceId(attendance.getId());
            breakSession.setBreakType(breakType);
            breakSession.setBreakStartTime(LocalDateTime.now());
            breakSessionRepo.save(breakSession);

            return "Break started: " + breakType.name();
        }



        @Override
       public SessionDetailResponse getSessionDetails(Long userId) {
           LocalDate today = LocalDate.now();
           LocalDate fromDate = today.minusDays(4);

           List<Attendance> attendances = attendanceRepository.findByUserIdAndDateBetween(userId, fromDate, today);
           List<Long> attendanceIds = attendances.stream().map(Attendance::getId).toList();

           List<BreakSession> allBreaks = breakSessionRepo.findByAttendanceIdIn(attendanceIds);

           // Group breaks by attendanceId
           Map<Long, List<BreakDTO>> breaksGrouped = allBreaks.stream().map(breakSession -> {
               BreakDTO dto = modelMapper.map(breakSession, BreakDTO.class);
               dto.setType(breakSession.getBreakType());
               dto.setStart(breakSession.getBreakStartTime());
               dto.setEnd(breakSession.getBreakEndTime());
               dto.setDuration(formatDuration(breakSession.getBreakDuration()));
               return Map.entry(breakSession.getAttendanceId(), dto);
           }).collect(Collectors.groupingBy(Map.Entry::getKey,
                   Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

           // Map attendances to DaySessionDTO
           List<DaySessionDTO> sessionList = attendances.stream().map(att -> {
               DaySessionDTO dto = modelMapper.map(att, DaySessionDTO.class);
               dto.setTotalWorkDuration(formatDuration(att.getTotalWorkDuration()));
               dto.setBreaks(breaksGrouped.getOrDefault(att.getId(), List.of()));
               return dto;
           }).toList();

           SessionDetailResponse response = new SessionDetailResponse();
           response.setUserId(userId);
           response.setSessions(sessionList);

           return response;
       }

        @Override
        public CheckInResponseDTO checkIn(Long userId) {
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();

            Optional<Attendance> optionalAttendance = attendanceRepository.findByUserIdAndDate(userId, today);

            if (optionalAttendance.isEmpty()) {
                // First clock-in of the day
                Attendance attendance = new Attendance();
                attendance.setUserId(userId);
                attendance.setDate(today);
                attendance.setCheckInTime(now);
                attendance.setTotalBreakDuration(Duration.ZERO);
                attendance.setTotalWorkDuration(Duration.ZERO);
                attendance.setStatus(AttendanceStatus.PRESENT);
                attendanceRepository.save(attendance);

                return new CheckInResponseDTO("Daily check-in successful", now);
            } else {
                // Already checked in — treat as break resume
                Attendance attendance = optionalAttendance.get();

                if (attendance.getCheckOutTime() != null) {
                    return new CheckInResponseDTO("Already checked out for the day", attendance.getCheckOutTime());
                }

                // You could optionally start/resume working timer here (if needed)

                return new CheckInResponseDTO("Resumed after break", now);
            }
        }


        @Override
        public String checkOut(Long userId) {
            LocalDate today = LocalDate.now();
            Attendance attendance = attendanceRepository.findByUserIdAndDate(userId, today)
                    .orElseThrow(() -> new RuntimeException("Please check-in first"));

            if (attendance.getCheckOutTime() != null) {
                return "Already checked out";
            }

            LocalTime checkOutTime = LocalTime.now();
            attendance.setCheckOutTime(checkOutTime);

            Duration fullSession = Duration.between(attendance.getCheckInTime(), checkOutTime);

            List<BreakSession> breaks = breakService.getBreaksByAttendanceId(attendance.getId());
            Duration totalBreakDuration = attendance.getTotalBreakDuration(); // all breaks
            Duration effectiveBreak = breakService.calculateEffectiveBreaks(breaks); // excluding RESTROOM

            // Check for extra break
            Duration extraBreak = Duration.ZERO;
            if (effectiveBreak.getSeconds() > 3600) {
                extraBreak = effectiveBreak.minus(Duration.ofHours(1));
            }

            Duration adjustedWorkDuration = fullSession.minus(totalBreakDuration).plus(extraBreak);
            attendance.setTotalWorkDuration(adjustedWorkDuration);

            attendanceRepository.save(attendance);

            boolean isComplete = adjustedWorkDuration.compareTo(Duration.ofHours(8)) >= 0;

            return STR."Check-out successful. Worked: \{adjustedWorkDuration}\{isComplete ? " (Complete)" : " (Incomplete - need 8 hrs)"}";
        }


        private Duration calculateWorkDuration(Attendance attendance) {
            Duration sessionDuration = Duration.between(attendance.getCheckInTime(), LocalTime.now());
            return sessionDuration.minus(attendance.getTotalBreakDuration());
        }

        private String formatDuration(Duration duration) {
            if (duration == null) return "00:00";
            long minutes = duration.toMinutes();
            return String.format("%02d:%02d", minutes / 60, minutes % 60);
        }


    }