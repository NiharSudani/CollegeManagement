package com.example.courseservice.repository;

import com.example.courseservice.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByCourseCode(String courseCode);
    List<Course> findByTitleContainingIgnoreCase(String title);
    List<Course> findByCourseCodeContainingIgnoreCase(String courseCode);
}
