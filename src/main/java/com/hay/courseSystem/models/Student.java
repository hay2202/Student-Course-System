package com.hay.courseSystem.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String email;
    @Column(unique = true)
    @NonNull
    private String specialKey;

    @ManyToMany
    @JoinTable(
            name = "student_course", // The name of the join table
            joinColumns = @JoinColumn(name = "student_id"), // Foreign key for the student
            inverseJoinColumns = @JoinColumn(name = "course_id") // Foreign key for the course
    )
    private List<Course> courses = new ArrayList<>();

}
