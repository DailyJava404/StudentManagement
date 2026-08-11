package com.example.studentsmanagement.Service;

import com.example.studentsmanagement.Entity.StudentInfo;
import com.example.studentsmanagement.Model.Request.DeleteRequest;
import com.example.studentsmanagement.Model.Response.ApiResponse;
import com.example.studentsmanagement.Interface.IStudentManagementService;
import com.example.studentsmanagement.Repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentManagementService implements IStudentManagementService {

    private final StudentRepository _studentRepository;
    public StudentManagementService(StudentRepository studentRepository) {
        _studentRepository = studentRepository;
    }

    @Override
    public ApiResponse<Void> createStudent(StudentInfo studentInfo) {
        try {
            _studentRepository.save(studentInfo);
            return ApiResponse.success("Student created");
        }
        catch (Exception e) {
            return ApiResponse.fail(500,"Failed to create student");
        }
    }

    @Override
    public ApiResponse<List<StudentInfo>> getAllStudents() {
        List<StudentInfo> getAllStudents = _studentRepository.findAll();
        return ApiResponse.success(getAllStudents);
    }

    @Override
    public ApiResponse<StudentInfo> getStudentById(Long id) {
        Optional<StudentInfo> getStudentBy = _studentRepository.findById(id);
        return getStudentBy.map(ApiResponse::success).orElseGet(() -> ApiResponse.fail(500, "Student not found"));
    }

    @Override
    public ApiResponse<Void> updateStudent(StudentInfo studentInfo) {
        try {
            StudentInfo existing = _studentRepository.findById(studentInfo.getStudentId())
                    .orElse(null);
            if (existing == null) {
                return ApiResponse.fail(500, "Student not found");
            }

            existing.setFirstName(studentInfo.getFirstName());
            existing.setLastName(studentInfo.getLastName());
            existing.setDateOfBirth(studentInfo.getDateOfBirth());
            existing.setGender(studentInfo.getGender());
            existing.setEmail(studentInfo.getEmail());
            existing.setPhone(studentInfo.getPhone());
            existing.setAddress(studentInfo.getAddress());
            _studentRepository.save(existing);
            return ApiResponse.success("Student updated successfully");
        }
        catch (Exception e) {
            return ApiResponse.fail(500,"Failed to update student");
        }

    }

    @Override
    public ApiResponse<Void> deleteStudent(DeleteRequest studentId) {
        try {
            if (!_studentRepository.existsById(studentId.getStudentId())) {
                return ApiResponse.fail(500, "Student not found");
            }
            _studentRepository.deleteById(studentId.getStudentId());
            return ApiResponse.success("Student deleted successfully");
        }
        catch (Exception e) {
            return ApiResponse.fail(500,"Failed to delete student");
        }
    }
}
