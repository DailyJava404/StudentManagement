package com.example.studentsmanagement.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "StudentInfo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StudentId")
    private Long studentId;

    @Column(name = "FirstName", nullable = false)
    private String firstName;

    @Column(name = "LastName", nullable = false)
    private String lastName;

    @Column(name = "DateOfBirth")
    private LocalDate dateOfBirth;

    @Column(name = "Gender")
    private String gender;

    @Column(name = "Email")
    private String email;

    @Column(name = "Phone")
    private String phone;

    @Column(name = "Address")
    private String address;

    @Column(name = "CreatedOn", updatable = false)
    private Date createdOn;

    @Column(name = "CreatedBy", updatable = false)
    private String createdBy;

    @Column(name = "ModifiedOn")
    private Date modifiedOn;

    @Column(name = "ModifiedBy")
    private String modifiedBy;

    @PrePersist
    public void onCreate() {
        Date now = new Date();

        createdOn = now;
        createdBy = "Admin";

        modifiedOn = now;
        modifiedBy = "Admin";
    }

    @PreUpdate
    public void onUpdate() {
        modifiedOn = new Date();
        modifiedBy = "Admin";
    }


}
