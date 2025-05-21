package com.tab.AttendanceApp.request;

import com.tab.AttendanceApp.enumeration.UserRole;
import lombok.Data;

import java.util.Date;

@Data
public class UserRequest {

    private Long id;
    private String name;
    private String email;
    private UserRole userRole;
    private Boolean isActive;
    private Long createdBy;
    private Date createdDate;
    private Long updatedBy;
    private Date updateDate;


}
