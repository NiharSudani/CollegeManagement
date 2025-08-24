package com.example.studentservice.repository;

import com.example.studentservice.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByRollNo(String rollNo);
    boolean existsByEmail(String email);
    
    List<Student> findByNameContainingIgnoreCase(String name);
    List<Student> findByDepartmentContainingIgnoreCase(String department);
    Student findByRollNo(String rollNo);
}
