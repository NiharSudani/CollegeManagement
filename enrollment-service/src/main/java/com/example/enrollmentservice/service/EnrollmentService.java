package com.example.enrollmentservice.service;

import com.example.enrollmentservice.client.CourseClient;
import com.example.enrollmentservice.client.StudentClient;
import com.example.enrollmentservice.dto.CreateEnrollmentDto;
import com.example.enrollmentservice.dto.EnrollmentDto;
import com.example.enrollmentservice.entity.Enrollment;
import com.example.enrollmentservice.repository.EnrollmentRepository;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentClient studentClient;
    private final CourseClient courseClient;

    @Transactional
    public EnrollmentDto createEnrollment(CreateEnrollmentDto createEnrollmentDto) {
        // Validate student exists
        try {
            studentClient.getStudentById(createEnrollmentDto.getStudentId());
        } catch (FeignException.NotFound e) {
            throw new IllegalArgumentException("Student not found with id: " + createEnrollmentDto.getStudentId());
        } catch (FeignException e) {
            throw new RuntimeException("Error communicating with student service", e);
        }

        // Validate course exists
        try {
            courseClient.getCourseById(createEnrollmentDto.getCourseId());
        } catch (FeignException.NotFound e) {
            throw new IllegalArgumentException("Course not found with id: " + createEnrollmentDto.getCourseId());
        } catch (FeignException e) {
            throw new RuntimeException("Error communicating with course service", e);
        }

        // Check for duplicate enrollment
        if (enrollmentRepository.existsByStudentIdAndCourseId(
                createEnrollmentDto.getStudentId(), createEnrollmentDto.getCourseId())) {
            throw new IllegalArgumentException("Student is already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(createEnrollmentDto.getStudentId());
        enrollment.setCourseId(createEnrollmentDto.getCourseId());

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return mapToDto(savedEnrollment);
    }

    @Transactional(readOnly = true)
    public EnrollmentDto getEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found with id: " + id));
        return mapToDto(enrollment);
    }

    @Transactional(readOnly = true)
    public Page<EnrollmentDto> getAllEnrollments(Pageable pageable) {
        return enrollmentRepository.findAll(pageable)
                .map(this::mapToDto);
    }

    @Transactional
    public void deleteEnrollment(Long id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Enrollment not found with id: " + id);
        }
        enrollmentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentDto> getEnrollmentsByStudentId(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EnrollmentDto> getEnrollmentsByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(this::mapToDto)
                .toList();
    }

    private EnrollmentDto mapToDto(Enrollment enrollment) {
        return new EnrollmentDto(
                enrollment.getId(),
                enrollment.getStudentId(),
                enrollment.getCourseId(),
                enrollment.getEnrolledAt()
        );
    }
}
