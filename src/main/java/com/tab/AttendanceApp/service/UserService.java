package com.tab.AttendanceApp.service;

import com.tab.AttendanceApp.entity.User;
import com.tab.AttendanceApp.exception.UserExistException;
import com.tab.AttendanceApp.request.UserRequest;

import java.util.List;

public interface UserService {
    List<User> getAllUser();

    Boolean saveUser(UserRequest request) throws UserExistException;
}
