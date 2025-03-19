package com.hay.courseSystem.models.requests;

import lombok.Data;

@Data
public class AdminLoginRequest {
    private String email;
    private String password;
}
