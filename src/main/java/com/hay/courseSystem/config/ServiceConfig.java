package com.hay.courseSystem.config;

import com.hay.courseSystem.services.StudentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    @Bean
    public StudentService studentService(){
        return new StudentService();
    }

}
