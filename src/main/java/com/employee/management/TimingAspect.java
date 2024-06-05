package com.employee.management;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class TimingAspect {

    private ThreadLocal<LocalDateTime> startTime = new ThreadLocal<>();

    @Before("execution(* com.employee.management.controllers.*.*(..))")
    public void beforeAdvice() {
        startTime.set(LocalDateTime.now());
    }

    @After("execution(* com.employee.management.controllers.*.*(..))")
    public void afterAdvice() {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime start = startTime.get();
        if (start != null) {
            long executionTime = java.time.Duration.between(start, endTime).toMillis();
            log.info("API Execution Time: " + executionTime + " milliseconds");
            startTime.remove();
        }
    }
}
