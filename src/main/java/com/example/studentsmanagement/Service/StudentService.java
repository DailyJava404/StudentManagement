package com.example.studentsmanagement.Service;

import com.example.studentsmanagement.Entity.StudentInfo;
import com.example.studentsmanagement.Model.Request.DeleteRequest;
import com.example.studentsmanagement.Model.Request.StudentIdRequest;
import com.example.studentsmanagement.Model.Request.StudentRequest;
import com.example.studentsmanagement.Model.Response.ApiResponse;
import com.example.studentsmanagement.Interface.IStudentService;
import com.example.studentsmanagement.Model.Response.StudentResponse;
import com.example.studentsmanagement.Repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Service
public class StudentService implements IStudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional
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
            if (studentRepository.save(studentInfo) != null)
                throw new RuntimeException("Testing rollback");
            return ApiResponse.success("Student created");
        } catch (Exception e) {
            logger.error("Failed to create student", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ApiResponse.fail(500, e.getMessage());
        }
    }

    @Override
    @Cacheable(value = "students", key = "'all'")
    public ApiResponse<List<StudentResponse>> getAllStudents() {
        List<StudentResponse> getAllStudents = studentRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
        return ApiResponse.success(getAllStudents);
    }

    @Override
    @Cacheable(value = "student", key = "#request.studentId()")
    public ApiResponse<StudentResponse> getStudentById(StudentIdRequest request) {
        return studentRepository.findById(request.studentId())
                .map(student -> ApiResponse.success(toResponse(student)))
                .orElseGet(() -> ApiResponse.fail(404, "Student not found"));
    }

    @Override
    @CachePut (value = "student", key = "#request.getStudentId()", unless = "#result.statusCode() != 200")
    public ApiResponse<StudentResponse> updateStudent(StudentRequest request) {
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
            return ApiResponse.success(toResponse(existing));
        } catch (Exception e) {
            return ApiResponse.fail(500, e.getMessage());
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
            return ApiResponse.fail(500,e.getMessage());
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
