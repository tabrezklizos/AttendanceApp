package com.tab.AttendanceApp.serviceImp;

import com.tab.AttendanceApp.entity.User;
import com.tab.AttendanceApp.enumeration.UserRole;
import com.tab.AttendanceApp.request.UserRequest;
import com.tab.AttendanceApp.response.AuthenticationResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.tab.AttendanceApp.repository.UserRepository;

@Service
public class AuthenticationService {


    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService,
                                 AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    //for new user registration
    public AuthenticationResponse register(UserRequest request) {

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setUserRole(UserRole.EMPLOYEE);

        user=repository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token);

    }

    //for login

    public AuthenticationResponse authenticate(UserRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user=repository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token);
    }




}
