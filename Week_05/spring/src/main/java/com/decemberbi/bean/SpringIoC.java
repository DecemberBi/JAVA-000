package com.decemberbi.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.Aware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Configuration
public class SpringIoC {

    @Bean("student2")
    public Student student2() {
        return new Student(2, "decemberbi2");
    }

    @Autowired
    @Qualifier("student2")
    private static Student student2;

    public static void main(String[] args) {
        // 方法1 使用xml方式注入
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Student student1 = (Student)context.getBean("student1");
        System.out.println(student1);
        // 方法2 使用注解注入
        System.out.println(student2);
    }
}
