package com.tab.AttendanceApp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String userRole;
    private Boolean isActive;
    private Long createdBy;
    private Date createdDate;
    private Long updatedBy;
    private Date updateDate;


}
