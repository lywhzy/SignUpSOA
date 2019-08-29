package lyw.demo.AOP;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class rizhi {

    @Around("execution(public * lyw.demo.service..*(..))")
    public Object around(ProceedingJoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getMethod().getName();
        Object obj = null;
        log.debug(Thread.currentThread().getName() + "正在执行" + methodSignature.getName() + methodName);
        try {
            obj = joinPoint.proceed(joinPoint.getArgs());
        } catch (Throwable throwable) {
            log.error(methodSignature.getName() + methodName + "--------" + throwable.getMessage());
            throwable.printStackTrace();
        }
        return obj;
    }

}
