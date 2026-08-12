package com.example.studentsmanagement.Interface;

import com.example.studentsmanagement.Model.Request.DeleteRequest;
import com.example.studentsmanagement.Model.Request.StudentIdRequest;
import com.example.studentsmanagement.Model.Request.StudentRequest;
import com.example.studentsmanagement.Model.Response.ApiResponse;
import com.example.studentsmanagement.Model.Response.StudentResponse;

import java.util.List;

public interface IStudentService {

    ApiResponse<Void> createStudent(StudentRequest request);

    ApiResponse<List<StudentResponse>>  getAllStudents();

    ApiResponse<StudentResponse>  getStudentById(StudentIdRequest id);

    ApiResponse<StudentResponse>  updateStudent(StudentRequest request);

    ApiResponse<Void>  deleteStudent(DeleteRequest studentId);

}
