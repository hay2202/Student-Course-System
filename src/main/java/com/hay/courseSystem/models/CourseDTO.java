package com.hay.courseSystem.models;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.NonNull;

@Data
public class CourseDTO {
    @NonNull
    private String courseName;
}
