package com.tab.AttendanceApp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseModel {

    @CreatedBy
    @Column(updatable = false)
    private Long createdBy;
    @CreatedDate
    @Column(updatable = false)
    private Date createdDate;
    @LastModifiedBy
    @Column(insertable = false)
    private Long updatedBy;
    @LastModifiedDate
    @Column(insertable = false)
    private Date updateDate;

}
