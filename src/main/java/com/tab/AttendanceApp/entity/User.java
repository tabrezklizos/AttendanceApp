package com.tab.AttendanceApp.entity;
import com.tab.AttendanceApp.enumeration.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
@Entity
@Data
@Table(name="user")
public class User extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    private Boolean isActive;
    private  Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;
}