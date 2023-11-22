package com.example.healthgenie.base.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

//@Configuration
@Aspect
public class TimeTraceApp {

    @Around("execution(* com.example.healthgenie..*(..))") // 패키지 하위에 모두 적용
    public Object execut(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("Start: " + joinPoint.toString());
        try {

            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("End: " + joinPoint + " " + timeMs + "ms");
        }
    }
}
