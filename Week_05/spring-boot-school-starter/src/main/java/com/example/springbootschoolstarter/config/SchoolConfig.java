package com.example.springbootschoolstarter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "school.starter.config")
public class SchoolConfig {

    private Integer studentId;

    private String studentName;
}
