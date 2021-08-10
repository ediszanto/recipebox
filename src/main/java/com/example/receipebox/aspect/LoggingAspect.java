package com.example.receipebox.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Aspect
@Configuration
@Slf4j
public class LoggingAspect {

    @Around("@annotation(com.example.receipebox.aspect.Log)")
    public void log(ProceedingJoinPoint joinPoint) throws Throwable {
        Object ret = joinPoint.proceed();
        String methodName = joinPoint.getSignature().getName();
        log.info("Method - " + methodName + " - was executed with: " + ret.toString());
    }
}
