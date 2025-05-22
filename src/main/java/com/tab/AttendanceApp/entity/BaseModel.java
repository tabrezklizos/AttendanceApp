package com.tab.AttendanceApp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Data
@MappedSuperclass
public class BaseModel {

    private Boolean isActive;
    private  Boolean isDeleted;
    private Long createdBy;
    private Date createdDate;
    private Long updatedBy;
    private Date updateDate;

}
