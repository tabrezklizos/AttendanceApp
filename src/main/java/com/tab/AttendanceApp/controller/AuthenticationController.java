package com.tab.AttendanceApp.controller;

import com.tab.AttendanceApp.request.UserRequest;
import com.tab.AttendanceApp.response.AuthenticationResponse;
import com.tab.AttendanceApp.serviceImp.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
             @RequestBody UserRequest request
    ) {

        return ResponseEntity.ok(authService.register(request));

    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody UserRequest request
    ) {

        return ResponseEntity.ok(authService.authenticate(request));
    }



}