package com.tab.AttendanceApp.controller;

import com.tab.AttendanceApp.request.BreakRequest;
import com.tab.AttendanceApp.service.BreakService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/breaks")
public class BreakController {

    private final BreakService breakService;

    public BreakController(BreakService breakService) {
        this.breakService = breakService;
    }

    @PostMapping("/start")
    public ResponseEntity<String> startBreak(@RequestBody BreakRequest request) {
        breakService.startBreak(request);
        return ResponseEntity.ok("Break started");
    }

    @PostMapping("/end")
    public ResponseEntity<String> endBreak(@RequestBody BreakRequest request) {
        breakService.endBreak(request);
        return ResponseEntity.ok("Break ended and duration tracked");
    }

}
