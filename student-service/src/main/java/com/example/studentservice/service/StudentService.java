package com.example.studentservice.service;

import com.example.studentservice.dto.CreateStudentDto;
import com.example.studentservice.dto.StudentDto;
import com.example.studentservice.dto.UpdateStudentDto;
import com.example.studentservice.entity.Student;
import com.example.studentservice.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    @Transactional
    public StudentDto createStudent(CreateStudentDto createStudentDto) {
        // Check for duplicate roll number
        if (studentRepository.existsByRollNo(createStudentDto.getRollNo())) {
            throw new IllegalArgumentException("Student with roll number " + createStudentDto.getRollNo() + " already exists");
        }

        // Check for duplicate email
        if (studentRepository.existsByEmail(createStudentDto.getEmail())) {
            throw new IllegalArgumentException("Student with email " + createStudentDto.getEmail() + " already exists");
        }

        Student student = new Student();
        student.setName(createStudentDto.getName());
        student.setRollNo(createStudentDto.getRollNo());
        student.setDepartment(createStudentDto.getDepartment());
        student.setEmail(createStudentDto.getEmail());
        student.setPhone(createStudentDto.getPhone());

        Student savedStudent = studentRepository.save(student);
        return mapToDto(savedStudent);
    }

    @Transactional(readOnly = true)
    public StudentDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + id));
        return mapToDto(student);
    }

    @Transactional(readOnly = true)
    public Page<StudentDto> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable)
                .map(this::mapToDto);
    }

    @Transactional
    public StudentDto updateStudent(Long id, UpdateStudentDto updateStudentDto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + id));

        // Check for duplicate email (excluding current student)
        if (!student.getEmail().equals(updateStudentDto.getEmail()) && 
            studentRepository.existsByEmail(updateStudentDto.getEmail())) {
            throw new IllegalArgumentException("Student with email " + updateStudentDto.getEmail() + " already exists");
        }

        student.setName(updateStudentDto.getName());
        student.setDepartment(updateStudentDto.getDepartment());
        student.setEmail(updateStudentDto.getEmail());
        student.setPhone(updateStudentDto.getPhone());

        Student updatedStudent = studentRepository.save(student);
        return mapToDto(updatedStudent);
    }

    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new EntityNotFoundException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public StudentDto getStudentByRollNo(String rollNo) {
        Student student = studentRepository.findAll().stream()
                .filter(s -> s.getRollNo().equals(rollNo))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Student not found with roll number: " + rollNo));
        return mapToDto(student);
    }

    @Transactional(readOnly = true)
    public List<StudentDto> searchStudentsByName(String name) {
        return studentRepository.findAll().stream()
                .filter(student -> student.getName().toLowerCase().contains(name.toLowerCase()))
                .map(this::mapToDto)
                .toList();
    }

    private StudentDto mapToDto(Student student) {
        return new StudentDto(
                student.getId(),
                student.getName(),
                student.getRollNo(),
                student.getDepartment(),
                student.getEmail(),
                student.getPhone()
        );
    }
}
