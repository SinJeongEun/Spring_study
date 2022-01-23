package com.example.aop2.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect  //aop
@Component //스프링에서 관리되도록
public class ParameterAop {

    @Pointcut("execution(* com.example.aop2.controller..*.*(..))")
    private void cut(){}

    @Before("cut()")
    public void before(JoinPoint joinPoint){

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        System.out.println(method.getName());
        //메소드에 들어가는 매개변수들 (배열)
        Object[]args = joinPoint.getArgs();

        for(Object obj : args){
            System.out.println("type : " + obj.getClass().getName());
            System.out.println("value : " + obj);
        }

    }

    @AfterReturning(value = "cut()",returning = "returnObj")
    public void afterReturn(JoinPoint joinPoint, Object returnObj){
        System.out.println("return obj");
        System.out.println(returnObj);

    }
}
