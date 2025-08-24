package com.example.courseservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courses", 
       uniqueConstraints = @UniqueConstraint(columnNames = "courseCode"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Course code is required")
    @Column(nullable = false, unique = true)
    private String courseCode;
    
    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;
    
    @Positive(message = "Credits must be positive")
    @Column(nullable = false)
    private Integer credits;
}
