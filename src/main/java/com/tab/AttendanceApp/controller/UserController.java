package com.tab.AttendanceApp.controller;

import com.tab.AttendanceApp.entity.User;
import com.tab.AttendanceApp.request.UserRequest;
import com.tab.AttendanceApp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@RequestBody UserRequest request){

        Boolean saveCategory = userService.saveUser(request);

        log.info(" @@@@@@@@@@@@@@@ UserRequest: {}",request);

        if(saveCategory){
            return new ResponseEntity<>("saved success",HttpStatus.CREATED);
        }

            return new ResponseEntity<>("not saved",HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/")
    public ResponseEntity<?> getAllUser(){

        List<User> allUser = userService.getAllUser();

        if (ObjectUtils.isEmpty(allUser)){
            return  ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(allUser,HttpStatus.OK);
    }






}
