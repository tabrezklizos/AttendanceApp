    package com.tab.AttendanceApp.serviceImp;

    import com.tab.AttendanceApp.dto.CheckInResponseDTO;
    import com.tab.AttendanceApp.entity.Attendance;
    import com.tab.AttendanceApp.enumeration.AttendanceStatus;
    import com.tab.AttendanceApp.repository.AttendanceRepository;
    import com.tab.AttendanceApp.service.AttendanceService;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.time.Duration;
    import java.time.LocalDate;
    import java.time.LocalTime;
    import java.util.Optional;

    @Slf4j
    @Service
    public class AttendanceServiceImpl implements AttendanceService {

        @Autowired
        private AttendanceRepository attendanceRepository;

        @Override
        public CheckInResponseDTO checkIn(Long userId) {
            LocalDate today = LocalDate.now();

            Optional<Attendance> existingAttendance = attendanceRepository.findByUserIdAndDate(userId, today);
            if (existingAttendance.isPresent()) {
                return new CheckInResponseDTO("Already checked in", existingAttendance.get().getCheckInTime());
            }

            Attendance attendance = new Attendance();
            attendance.setUserId(userId);
            attendance.setDate(today);
            attendance.setCheckInTime(LocalTime.now());
            attendance.setTotalBreakDuration(Duration.ZERO); // init
            attendance.setTotalWorkDuration(Duration.ZERO);
            attendance.setStatus(AttendanceStatus.PRESENT);

            attendanceRepository.save(attendance);

            if (attendance.getTotalWorkDuration().getSeconds() >= 70) {

                log.info("working day completed ");


            } else {
                log.info("working day not completed ");
            }


            return new CheckInResponseDTO("Check-in successful", attendance.getCheckInTime());
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
            Duration netWorkDuration = fullSession.minus(attendance.getTotalBreakDuration());

            attendance.setTotalWorkDuration(netWorkDuration);

            attendanceRepository.save(attendance);

            return "Check-out successful. Worked: " + netWorkDuration;
        }

        private Duration calculateWorkDuration(Attendance attendance) {
            Duration sessionDuration = Duration.between(attendance.getCheckInTime(), LocalTime.now());
            return sessionDuration.minus(attendance.getTotalBreakDuration());
        }


    }