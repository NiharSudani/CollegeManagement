package com.example.studentservice.service;

import com.example.studentservice.dto.CreateStudentDto;
import com.example.studentservice.dto.StudentDto;
import com.example.studentservice.entity.Student;
import com.example.studentservice.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchStudentsByName() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1L, "John Doe", "CS101", "john@example.com", "1234567890"));
        students.add(new Student(2L, "Jane Doe", "CS102", "jane@example.com", "0987654321"));

        when(studentRepository.findByNameContainingIgnoreCase("Doe")).thenReturn(students);

        List<StudentDto> result = studentService.searchStudentsByName("Doe");

        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Doe", result.get(1).getName());
    }

    @Test
    void testGetStudentByRollNo() {
        Student student = new Student(1L, "John Doe", "CS101", "john@example.com", "1234567890");

        when(studentRepository.findByRollNo("CS101")).thenReturn(student);

        StudentDto result = studentService.getStudentByRollNo("CS101");

        assertEquals("John Doe", result.getName());
        assertEquals("CS101", result.getRollNo());
    }

    @Test
    void testSearchStudentsByDepartment() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1L, "John Doe", "Computer Science", "john@example.com", "1234567890"));

        when(studentRepository.findByDepartmentContainingIgnoreCase("Computer Science")).thenReturn(students);

        List<StudentDto> result = studentService.searchStudentsByDepartment("Computer Science");

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }
}
