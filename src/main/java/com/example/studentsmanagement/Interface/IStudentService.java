package com.example.studentsmanagement.Interface;

import com.example.studentsmanagement.Entity.StudentInfo;
import com.example.studentsmanagement.Model.Request.DeleteRequest;
import com.example.studentsmanagement.Model.Response.ApiResponse;
import com.example.studentsmanagement.Model.Response.StudentResponse;

import java.util.List;

public interface IStudentManagementService {

    ApiResponse<Void> createStudent(StudentInfo studentInfo);

    ApiResponse<List<StudentInfo>>  getAllStudents();

    ApiResponse<StudentInfo>  getStudentById(Long id);

    ApiResponse<Void>  updateStudent(StudentInfo studentInfo);

    ApiResponse<Void>  deleteStudent(DeleteRequest studentId);

}
