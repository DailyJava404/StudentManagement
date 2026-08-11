package com.example.studentsmanagement.Controller;

import com.example.studentsmanagement.Entity.StudentInfo;
import com.example.studentsmanagement.Model.Request.DeleteRequest;
import com.example.studentsmanagement.Model.Request.StudentIdRequest;
import com.example.studentsmanagement.Model.Request.StudentRequest;
import com.example.studentsmanagement.Model.Response.ApiResponse;
import com.example.studentsmanagement.Interface.IStudentService;
import com.example.studentsmanagement.Model.Response.StudentResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final IStudentService _studentManagementService;
    public StudentController(IStudentService studentManagementService) {
        _studentManagementService = studentManagementService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Void>> createStudent(@Valid @RequestBody StudentRequest request)
    {
        ApiResponse<Void> result = _studentManagementService.createStudent(request);
        return ResponseEntity.status(result.statusCode()).body(result);
    }

    @GetMapping("/getAllStudents")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAllStudents()
    {
        ApiResponse<List<StudentResponse>> result = _studentManagementService.getAllStudents();
        return ResponseEntity.status(result.statusCode()).body(result);
    }

    @PostMapping("/getStudentById")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudentById(@Valid @RequestBody StudentIdRequest request)
    {
        ApiResponse<StudentResponse> result = _studentManagementService.getStudentById(request);
        return ResponseEntity.status(result.statusCode()).body(result);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<Void>> updateStudent(@Valid @RequestBody StudentRequest request)
    {
        ApiResponse<Void> result = _studentManagementService.updateStudent(request);
        return ResponseEntity.status(result.statusCode()).body(result);
    }

    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@Valid @RequestBody DeleteRequest request)
    {
        ApiResponse<Void> result = _studentManagementService.deleteStudent(request);
        return ResponseEntity.status(result.statusCode()).body(result);
    }

}
