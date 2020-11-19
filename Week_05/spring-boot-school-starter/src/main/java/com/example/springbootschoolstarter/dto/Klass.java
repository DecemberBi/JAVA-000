package com.example.springbootschoolstarter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
public class Klass { 
    
    List<Student> students;
    
}
