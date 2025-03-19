package com.hay.courseSystem.services;

import com.hay.courseSystem.models.Course;
import com.hay.courseSystem.models.Student;
import com.hay.courseSystem.repository.CourseRepository;
import com.hay.courseSystem.repository.StudentRepository;
import com.hay.courseSystem.services.auth.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hay.courseSystem.constans.Constans.*;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);


    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SessionManager sessionManager;

    public ResponseEntity<String> login(String specialKey) {
        logger.info("Student login attempt with special key: {}", specialKey);
        Student student = studentRepository.findBySpecialKey(specialKey);

        if (student == null) {
            logger.warn("Student login failed. Invalid special key: {}", specialKey);
            return ResponseEntity.badRequest().body("Invalid special key");
        }
        // Generate session and return the token
        return ResponseEntity.ok(sessionManager.createSession(student.getSpecialKey(), STUDENT));
    }

    public ResponseEntity<String> registerCourse(String sessionToken, Long courseId) {
        Student student = studentRepository.findBySpecialKey(sessionManager.getSpecialKeyBySession(sessionToken));
        Course course = courseRepository.findById(courseId).orElse(null);

        if (student == null || course == null) {
            logger.warn("Register course failed - Student or Course not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student or Course not found");
        }

        if (student.getCourses().contains(course)){
            logger.warn("Student '{}' already register for {} course", student.getName(), course.getName());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You already register for this course");
        }

        if (student.getCourses().size() >= 2) {
            logger.warn("Student '{}' attempted to register for more than 2 courses", student.getName());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Students can register for up to 2 courses only");
        }

        if (course.getStudents().size() >= 30) {
            logger.warn("Course '{}' is already full with 30 students", course.getName());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Course cannot have more than 30 students");
        }

        student.getCourses().add(course);
        studentRepository.save(student);  // Saves both the student and the course relationship
        logger.info("Student '{}' registered for course '{}'", student.getName(), course.getName());
        return ResponseEntity.ok("Student registered successfully");
    }


    public ResponseEntity<String> cancelRegister(String sessionToken, Long courseId) {
        Student student = studentRepository.findBySpecialKey(sessionManager.getSpecialKeyBySession(sessionToken));
        Course course = courseRepository.findById(courseId).orElse(null);

        if (student == null || course == null) {
            logger.warn("Cancel registration failed - Student or Course not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student or Course not found");
        }

        if (!student.getCourses().contains(course)) {
            logger.warn("Cancel registration failed - Student '{}' is not registered in course '{}'", student.getName(), course.getName());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student is not registered in this course");
        }

        student.getCourses().remove(course);
        studentRepository.save(student);  // Update the relationship in the database

        logger.info("Student '{}' canceled registration for course '{}'", student.getName(), course.getName());
        return ResponseEntity.ok("Student registration cancelled successfully");
    }

    public ResponseEntity<List<Course>> getStudentCourses(String sessionToken) {
        Student student = studentRepository.findBySpecialKey(sessionManager.getSpecialKeyBySession(sessionToken));
        return ResponseEntity.ok(student.getCourses());
    }
}
