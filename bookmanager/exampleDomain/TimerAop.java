package com.jpa.bookmanager.exampleDomain;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect //aop 사용
@Component
public class TimerAop {
//    @Pointcut("execution(* com.jpa.bookmanager.exampleDomain.ReservationRepository..*.*(..))")
//    private void cut(){}

    @Pointcut("@annotation(com.jpa.bookmanager.exampleDomain.Timer)")
    private void enableTimer() {}

    @Around("enableTimer()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = joinPoint.proceed();

        stopWatch.stop();

        System.out.println("경과 시간 : " + stopWatch.getTotalTimeMillis());

        return result;
    }
}
