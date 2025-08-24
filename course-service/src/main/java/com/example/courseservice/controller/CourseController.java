package com.example.courseservice.controller;

import com.example.courseservice.dto.CreateCourseDto;
import com.example.courseservice.dto.CourseDto;
import com.example.courseservice.dto.UpdateCourseDto;
import com.example.courseservice.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CreateCourseDto createCourseDto) {
        CourseDto createdCourse = courseService.createCourse(createCourseDto);
        return ResponseEntity.created(URI.create("/api/courses/" + createdCourse.getId()))
                .body(createdCourse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        CourseDto course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    @GetMapping
    public ResponseEntity<Page<CourseDto>> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        Sort sort = sortDirection.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<CourseDto> courses = courseService.getAllCourses(pageable);
        return ResponseEntity.ok(courses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateCourseDto updateCourseDto) {
        CourseDto updatedCourse = courseService.updateCourse(id, updateCourseDto);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<CourseDto>> searchCourses(
            @RequestParam(required = false) String courseCode,
            @RequestParam(required = false) String title) {
        
        // For simplicity, we'll implement search in the service layer
        // This would typically use repository methods with proper search queries
        List<CourseDto> allCourses = courseService.getAllCourses(Pageable.unpaged()).getContent();
        
        if (courseCode != null) {
            List<CourseDto> filtered = allCourses.stream()
                    .filter(course -> course.getCourseCode().toLowerCase().contains(courseCode.toLowerCase()))
                    .toList();
            return ResponseEntity.ok(filtered);
        } else if (title != null) {
            List<CourseDto> filtered = allCourses.stream()
                    .filter(course -> course.getTitle().toLowerCase().contains(title.toLowerCase()))
                    .toList();
            return ResponseEntity.ok(filtered);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
