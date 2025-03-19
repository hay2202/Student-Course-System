package com.hay.courseSystem.controllers;

import com.hay.courseSystem.models.Course;
import com.hay.courseSystem.models.Student;
import com.hay.courseSystem.models.StudentDTO;
import com.hay.courseSystem.models.requests.AdminLoginRequest;
import com.hay.courseSystem.models.requests.CourseRequest;
import com.hay.courseSystem.models.requests.StudentRequest;
import com.hay.courseSystem.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<String> adminLogin(@RequestBody AdminLoginRequest request) {
        return adminService.login(request.getEmail(), request.getPassword());
    }

    @PostMapping("/students")
    public ResponseEntity<?> createStudent(@RequestBody StudentRequest request) {
        return adminService.createStudent(request);
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        return ResponseEntity.ok(adminService.getAllStudents());
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id){
        return adminService.getStudentById(id);
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @RequestBody StudentRequest request) {
        return adminService.updateStudent(id, request);
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        return adminService.deleteStudent(id);
    }

    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody CourseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createCourse(request));
    }

    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(adminService.getAllCourses());
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Course course = adminService.getCourseById(id);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(course);
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody CourseRequest request) {
        Course course = adminService.updateCourse(id, request);
        if (course == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(course);
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        adminService.deleteCourse(id);
        return ResponseEntity.ok("Course with id " + id + " was deleted");
    }
}
