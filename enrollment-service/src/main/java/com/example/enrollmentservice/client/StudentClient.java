package com.example.enrollmentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "student-service", url = "http://localhost:8081")
public interface StudentClient {
    
    @GetMapping("/api/students/{id}")
    Object getStudentById(@PathVariable Long id);
}
