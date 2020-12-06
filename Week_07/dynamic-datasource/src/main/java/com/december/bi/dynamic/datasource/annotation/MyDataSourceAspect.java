package com.december.bi.dynamic.datasource.annotation;

import com.december.bi.dynamic.datasource.config.MyDataSourceHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class MyDataSourceAspect {

    @Pointcut("@annotation(com.december.bi.dynamic.datasource.annotation.MyDataSource)")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("around start...");
        Object result;
        Class<?> aClass = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        Method method = aClass.getMethod(methodName);
        MyDataSource annotation = method.getAnnotation(MyDataSource.class);
        if (annotation == null) {
            MyDataSourceHolder.setDataSource("master");
        } else {
            MyDataSourceHolder.setDataSource(annotation.value());
        }
        result = joinPoint.proceed();
        log.info("around end...");
        return result;
    }

}
