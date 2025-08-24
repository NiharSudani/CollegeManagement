package com.example.studentservice.controller;

import com.example.studentservice.dto.CreateStudentDto;
import com.example.studentservice.dto.StudentDto;
import com.example.studentservice.dto.UpdateStudentDto;
import com.example.studentservice.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@Valid @RequestBody CreateStudentDto createStudentDto) {
        StudentDto createdStudent = studentService.createStudent(createStudentDto);
        return ResponseEntity.created(URI.create("/api/students/" + createdStudent.getId()))
                .body(createdStudent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        StudentDto student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @GetMapping
    public ResponseEntity<Page<StudentDto>> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        Sort sort = sortDirection.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<StudentDto> students = studentService.getAllStudents(pageable);
        return ResponseEntity.ok(students);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateStudentDto updateStudentDto) {
        StudentDto updatedStudent = studentService.updateStudent(id, updateStudentDto);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentDto>> searchStudents(
            @RequestParam(required = false) String rollNo,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department) {
        
        if (rollNo != null) {
            StudentDto student = studentService.getStudentByRollNo(rollNo);
            return ResponseEntity.ok(List.of(student));
        } else if (name != null) {
            List<StudentDto> students = studentService.searchStudentsByName(name);
            return ResponseEntity.ok(students);
        } else if (department != null) {
            List<StudentDto> students = studentService.searchStudentsByDepartment(department);
            return ResponseEntity.ok(students);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
