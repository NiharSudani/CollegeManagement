package com.example.courseservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseDto {
    
    @NotBlank(message = "Course code is required")
    private String courseCode;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @Positive(message = "Credits must be positive")
    private Integer credits;
}
