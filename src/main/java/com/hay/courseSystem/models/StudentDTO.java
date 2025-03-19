package com.hay.courseSystem.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudentDTO {
    private Long id;
    private String name;
    private String email;
    private String specialKey;
    private List<String> courses;

    public StudentDTO(Long id, String name, String email, String specialKey, List<String> courses) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.specialKey = specialKey;
        this.courses = courses;
    }
}
