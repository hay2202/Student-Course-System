package com.hay.courseSystem.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String description;


    @ManyToMany(mappedBy = "courses")
    private List<Student> students = new ArrayList<>();

    @JsonProperty("students")
    public List<String> getStudentNames() {
        return students.stream()
                .map(Student::getName)
                .collect(Collectors.toList());
    }
}
