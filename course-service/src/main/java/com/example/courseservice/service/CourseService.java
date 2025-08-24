package com.example.courseservice.service;

import com.example.courseservice.dto.CreateCourseDto;
import com.example.courseservice.dto.CourseDto;
import com.example.courseservice.dto.UpdateCourseDto;
import com.example.courseservice.entity.Course;
import com.example.courseservice.repository.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    @Transactional
    public CourseDto createCourse(CreateCourseDto createCourseDto) {
        if (courseRepository.existsByCourseCode(createCourseDto.getCourseCode())) {
            throw new IllegalArgumentException("Course with code " + createCourseDto.getCourseCode() + " already exists");
        }

        Course course = new Course();
        course.setCourseCode(createCourseDto.getCourseCode());
        course.setTitle(createCourseDto.getTitle());
        course.setCredits(createCourseDto.getCredits());

        Course savedCourse = courseRepository.save(course);
        return mapToDto(savedCourse);
    }

    @Transactional(readOnly = true)
    public CourseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + id));
        return mapToDto(course);
    }

    @Transactional(readOnly = true)
    public Page<CourseDto> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable)
                .map(this::mapToDto);
    }

    @Transactional
    public CourseDto updateCourse(Long id, UpdateCourseDto updateCourseDto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + id));

        course.setTitle(updateCourseDto.getTitle());
        course.setCredits(updateCourseDto.getCredits());

        Course updatedCourse = courseRepository.save(course);
        return mapToDto(updatedCourse);
    }

    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new EntityNotFoundException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }

    private CourseDto mapToDto(Course course) {
        return new CourseDto(
                course.getId(),
                course.getCourseCode(),
                course.getTitle(),
                course.getCredits()
        );
    }
}
