package com.example.studentsmanagement.Repository;

import com.example.studentsmanagement.Entity.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentManagementRepository extends JpaRepository<StudentInfo, Long> {

}
