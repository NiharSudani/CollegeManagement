package com.example.enrollmentservice.client;

import com.example.enrollmentservice.dto.CourseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "course-service", url = "http://localhost:8082")
public interface CourseClient {
    
    @GetMapping("/api/courses/{id}")
    CourseDto getCourseById(@PathVariable Long id);
}
