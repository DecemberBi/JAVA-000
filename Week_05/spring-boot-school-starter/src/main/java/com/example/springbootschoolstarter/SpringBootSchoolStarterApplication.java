package com.example.springbootschoolstarter;

import com.example.springbootschoolstarter.dto.School;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringBootSchoolStarterApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootSchoolStarterApplication.class, args);
        School bean = context.getBean(School.class);
        System.out.println(bean);

    }

}
