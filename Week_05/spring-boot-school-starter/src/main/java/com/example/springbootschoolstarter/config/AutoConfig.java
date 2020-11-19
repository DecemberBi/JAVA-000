package com.example.springbootschoolstarter.config;

import com.example.springbootschoolstarter.dto.Klass;
import com.example.springbootschoolstarter.dto.School;
import com.example.springbootschoolstarter.dto.Student;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@ConditionalOnClass(value = {School.class, Klass.class, Student.class})
@ConditionalOnProperty(prefix = "school.config", value = "enable", havingValue = "true")
@EnableConfigurationProperties(SchoolConfig.class)
public class AutoConfig {

    @Bean
    @ConditionalOnMissingBean(value = School.class)
    public School getSchool(SchoolConfig schoolConfig) {
        Student student = new Student(schoolConfig.getStudentId(), schoolConfig.getStudentName());
        Klass klass = new Klass(Arrays.asList(student));
        School school = new School(klass);
        return school;
    }
}
