package com.example.aop2.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class TimerAop {

    @Pointcut("execution(* com.example.aop2.controller..*.*(..))")
    private void cut(){}

    @Pointcut("@annotation(com.example.aop2.annotation.Timer))")
    private void enableTimer(){}

    @Around("cut() && enableTimer()")
    private void around(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = joinPoint.proceed(); // 실행 되는 시점점

        stopWatch.stop();

        System.out.println("total time : " + stopWatch.getTotalTimeSeconds());
    }
}
