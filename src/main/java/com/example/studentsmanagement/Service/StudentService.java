package com.example.studentsmanagement.Service;

import com.example.studentsmanagement.Entity.StudentInfo;
import com.example.studentsmanagement.Model.Request.DeleteRequest;
import com.example.studentsmanagement.Model.Request.StudentIdRequest;
import com.example.studentsmanagement.Model.Request.StudentRequest;
import com.example.studentsmanagement.Model.Response.ApiResponse;
import com.example.studentsmanagement.Interface.IStudentService;
import com.example.studentsmanagement.Model.Response.StudentResponse;
import com.example.studentsmanagement.Repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService implements IStudentService {

    private final StudentRepository studentRepository;
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public ApiResponse<Void> createStudent(StudentRequest request) {
        try {
            boolean exists = studentRepository
                    .existsByFirstNameIgnoreCase(request.getFirstName(), request.getLastName());

            if (exists) {
                return ApiResponse.fail(
                        409,
                        "A student with this name already exists"
                );
            }
            StudentInfo studentInfo = new StudentInfo();
            studentInfo.setFirstName(request.getFirstName());
            studentInfo.setLastName(request.getLastName());
            studentInfo.setGender(request.getGender());
            studentInfo.setEmail(request.getEmail());
            studentInfo.setPhone(request.getPhone());
            studentInfo.setAddress(request.getAddress());
            studentInfo.setDateOfBirth(request.getDateOfBirth());
            studentRepository.save(studentInfo);
            return ApiResponse.success("Student created");
        } catch (Exception e) {
            return ApiResponse.fail(500, "Failed to create student");
        }
    }

    @Override
    public ApiResponse<List<StudentResponse>> getAllStudents() {
        List<StudentResponse> getAllStudents = studentRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
        return ApiResponse.success(getAllStudents);
    }

    @Override
    public ApiResponse<StudentResponse> getStudentById(StudentIdRequest request) {
        return studentRepository.findById(request.studentId())
                .map(student -> ApiResponse.success(toResponse(student)))
                .orElseGet(() -> ApiResponse.fail(404, "Student not found"));
    }

    @Override
    public ApiResponse<Void> updateStudent(StudentRequest request) {
        try {
            StudentInfo existing = studentRepository.findById(request.getStudentId()).orElse(null);
            if (existing == null) {
                return ApiResponse.fail(404, "Student not found");
            }
            existing.setFirstName(request.getFirstName());
            existing.setLastName(request.getLastName());
            existing.setGender(request.getGender());
            existing.setEmail(request.getEmail());
            existing.setPhone(request.getPhone());
            existing.setAddress(request.getAddress());
            existing.setDateOfBirth(request.getDateOfBirth());
            studentRepository.save(existing);
            return ApiResponse.success("Student updated");
        } catch (Exception e) {
            return ApiResponse.fail(500, "Failed to update student");
        }
    }

    @Override
    public ApiResponse<Void> deleteStudent(DeleteRequest studentId) {
        try {
            if (!studentRepository.existsById(studentId.studentId())) {
                return ApiResponse.fail(404, "Student not found");
            }
            studentRepository.deleteById(studentId.studentId());
            return ApiResponse.success("Student deleted successfully");
        }
        catch (Exception e) {
            return ApiResponse.fail(500,"Failed to delete student");
        }
    }

    private StudentResponse toResponse(StudentInfo student) {
        StudentResponse response = new StudentResponse();

        response.setStudentId(student.getStudentId());
        response.setFirstName(student.getFirstName());
        response.setLastName(student.getLastName());
        response.setDateOfBirth(student.getDateOfBirth());
        response.setGender(student.getGender());
        response.setEmail(student.getEmail());
        response.setPhone(student.getPhone());
        response.setAddress(student.getAddress());

        return response;
    }
}
