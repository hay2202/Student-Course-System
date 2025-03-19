package com.hay.courseSystem.controllers;

import com.hay.courseSystem.models.Course;
import com.hay.courseSystem.models.requests.StudentLoginRequest;
import com.hay.courseSystem.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody StudentLoginRequest request) {
        return studentService.login(request.getSpecialKey());
    }

    @PostMapping("/register/{courseId}")
    public ResponseEntity<String> registerCourse(@RequestHeader("Authorization") String token, @PathVariable Long courseId) {
        return studentService.registerCourse(token, courseId);
    }


    @DeleteMapping("/cancel/{courseId}")
    public ResponseEntity<String> cancelRegistration(@RequestHeader("Authorization") String token, @PathVariable Long courseId) {
        return studentService.cancelRegister(token, courseId);
    }

    @GetMapping("/myCourses")
    public ResponseEntity<List<Course>> getStudentCourses(@RequestHeader("Authorization") String token){
        return studentService.getStudentCourses(token);
    }

}
