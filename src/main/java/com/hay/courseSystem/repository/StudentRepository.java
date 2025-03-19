package com.hay.courseSystem.repository;

import com.hay.courseSystem.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    Student findByEmail(String email);
    Student findBySpecialKey(String specialKey);

    boolean existsBySpecialKey(String specialKey);
}
