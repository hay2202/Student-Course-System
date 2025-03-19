package com.hay.courseSystem.services;

import com.hay.courseSystem.models.Course;
import com.hay.courseSystem.models.Student;
import com.hay.courseSystem.models.StudentDTO;
import com.hay.courseSystem.models.requests.CourseRequest;
import com.hay.courseSystem.models.requests.StudentRequest;
import com.hay.courseSystem.repository.CourseRepository;
import com.hay.courseSystem.repository.StudentRepository;
import com.hay.courseSystem.services.auth.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import static com.hay.courseSystem.constans.Constans.*;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    private static final String PREDEFINED_ADMIN_EMAIL = "admin@admin.com";
    private static final String PREDEFINED_ADMIN_PASSWORD = "$2b$12$xT.d.PyOnEO2ChDD5yweIOAP9YTs6ZKmy7jaBp2mP50V6xlbQiToy"; //bcrypt-hashed password (admin)
    private final PasswordEncoder passwordEncoder;
    private final SessionManager sessionManager;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    public AdminService(PasswordEncoder passwordEncoder, SessionManager sessionManager) {
        this.passwordEncoder = passwordEncoder;
        this.sessionManager = sessionManager;
    }


    /**
     * Authenticate admin using email and password.
     *
     * @param email    The email provided by the admin.
     * @param password The password provided by the admin.
     * @return sessionToken The generated session token if authentication is successful.
     */
    public ResponseEntity<String> login(String email, String password){
        if (PREDEFINED_ADMIN_EMAIL.equals(email) && passwordEncoder.matches(password, PREDEFINED_ADMIN_PASSWORD)) {
            // If credentials are valid, generate a session token
            logger.info("Admin login successful.");
            return ResponseEntity.status(HttpStatus.CREATED).body(sessionManager.createSession(ADMIN,ADMIN));
        }
        logger.warn("Admin login failed for email: {}", email);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }

    public List<StudentDTO> getAllStudents() {
        logger.info("Fetching all students with their registered courses");
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(student -> {
                    // Extract course names into a list of strings
                    List<String> courseNames = student.getCourses().stream()
                            .map(Course::getName)  // Extract only course names
                            .collect(Collectors.toList());

                    // Return the StudentDTO with course names
                    return new StudentDTO(student.getId(), student.getName(), student.getEmail(), student.getSpecialKey(), courseNames);
                })
                .collect(Collectors.toList());
    }

    public List<Course> getAllCourses() {
        logger.info("Fetching all courses with registered students");
        return courseRepository.findAll();
    }

    public ResponseEntity<?> createStudent(StudentRequest request) {
        logger.info("Creating student");
        Student student = new Student(request.getName(), request.getEmail(), generateSpecialKey());
        try{
            studentRepository.save(student);
        } catch (DataIntegrityViolationException e){
            logger.error("Failed to create student");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exist");
        }

        logger.info("Student created with ID: {}", student.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }

    public ResponseEntity<StudentDTO> getStudentById(Long id) {
        logger.info("Fetching student by ID: {}", id);
//        return studentRepository.findById(id).orElse(null);
        Student student = studentRepository.findById(id).orElse(null);

        if (student != null) {
            List<String> courseNames = student.getCourses().stream()
                    .map(Course::getName)  // Extract only course names
                    .collect(Collectors.toList());

            StudentDTO studentDTO = new StudentDTO(
                    student.getId(),
                    student.getName(),
                    student.getEmail(),
                    student.getSpecialKey(),
                    courseNames
            );

            // Return ResponseEntity with the StudentDTO and status code OK (200)
            return ResponseEntity.ok(studentDTO);
        }

        // If the student is not found, return a 404 status
        return ResponseEntity.notFound().build();

    }

    public ResponseEntity<StudentDTO> updateStudent(Long id, StudentRequest request) {
        logger.info("Updating student with ID: {}", id);
        Student student = studentRepository.findById(id).orElse(null);

        if (student != null) {
            if (request.getName() != null) {
                student.setName(request.getName());
            }
            if (request.getEmail() != null) {
                student.setEmail(request.getEmail());
            }

            Student updatedStudent = studentRepository.save(student);

            List<String> courseNames = updatedStudent.getCourses().stream()
                    .map(Course::getName)
                    .collect(Collectors.toList());

            StudentDTO studentDTO = new StudentDTO(
                    updatedStudent.getId(),
                    updatedStudent.getName(),
                    updatedStudent.getEmail(),
                    updatedStudent.getSpecialKey(),
                    courseNames
            );

            return ResponseEntity.ok(studentDTO);
        }

        logger.warn("Student with ID {} not found", id);
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<String> deleteStudent(Long id) {
        logger.info("Deleting student with ID: {}", id);
        studentRepository.deleteById(id);
        return ResponseEntity.ok("student with id " + id + " deleted successfully");
    }

    public Course createCourse(CourseRequest request) {
        Course course = new Course(request.getName(), request.getDescription());
        logger.info("Course created with ID: {}", course.getId());
        return courseRepository.save(course);
    }

    public Course getCourseById(Long id) {
        logger.info("Fetching course by ID: {}", id);
        return courseRepository.findById(id).orElse(null);
    }

    public Course updateCourse(Long id, CourseRequest request) {
        Course course = getCourseById(id);
        if (course != null){
            if (request.getName() != null)
                course.setName(request.getName());
            if (request.getDescription() != null)
                course.setDescription(request.getDescription());
            logger.info("Course updated successfully: {}", course);
            return courseRepository.save(course);
        }
        logger.warn("Course with ID {} not found", id);
        return null;
    }

    public void deleteCourse(Long id) {
        logger.info("Deleting course with ID: {}", id);
        courseRepository.deleteById(id);
    }

    private String generateSpecialKey() {
        String key;
        do {
            key = String.format("%09d", new Random().nextInt(1_000_000_000));
        } while (studentRepository.existsBySpecialKey(key)); // Check for uniqueness
        logger.info("Generated unique special key: {}", key);
        return key;
    }

}
