package com.example.studentsmanagement.Controller;

import com.example.studentsmanagement.Entity.StudentInfo;
import com.example.studentsmanagement.Model.Request.DeleteRequest;
import com.example.studentsmanagement.Model.Response.ApiResponse;
import com.example.studentsmanagement.Model.Response.StudentResponse;
import com.example.studentsmanagement.Interface.IStudentManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentsManagementController {

    private final IStudentManagementService _studentManagementService;
    public StudentsManagementController(IStudentManagementService studentManagementService) {
        _studentManagementService = studentManagementService;
    }

    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Void>> createStudent(@RequestBody StudentInfo studentInfo)
    {
        return ResponseEntity.ok(_studentManagementService.createStudent(studentInfo));
    }

    @GetMapping("/getAllStudents")
    public ResponseEntity<ApiResponse<List<StudentInfo>>> getAllStudents()
    {
        return ResponseEntity.ok(_studentManagementService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentInfo>> getStudentById(@PathVariable Long id)
    {
        return ResponseEntity.ok(_studentManagementService.getStudentById(id));
    }

    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/update")
    public ResponseEntity<ApiResponse<Void>> updateStudent(@RequestBody StudentInfo studentInfo)
    {
        return ResponseEntity.ok(_studentManagementService.updateStudent(studentInfo));
    }

    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@RequestBody DeleteRequest studentId)
    {
        return ResponseEntity.ok(_studentManagementService.deleteStudent(studentId));
    }

}
