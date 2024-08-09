package com.example.demo.aop

import com.example.demo.annotations.MyInterceptor
import io.opentelemetry.instrumentation.api.instrumenter.LocalRootSpan
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@Aspect
class MyInterceptorAop {

    private val logger: Logger = LoggerFactory.getLogger(LoggerAspect::class.java)

    @Before("@annotation(com.example.demo.annotations.MyInterceptor)")
    fun before(jointPoint: JoinPoint) {
        logger.info("Before MyInterceptor aspects");
    }

    @Around("@annotation(com.example.demo.annotations.MyInterceptor)")
    fun around(jointPoint: ProceedingJoinPoint): Any? {
        logger.info("*** Before MyInterceptor ***");
        val res = (jointPoint.proceed() as Map<String, String>).toMutableMap();
        res["around"] = "Success"
        logger.info("*** After MyInterceptor : {}", res)
        return res;
    }

    @After("@annotation(com.example.demo.annotations.MyInterceptor)")
    fun after(jointPoint: JoinPoint) {
        logger.info("After MyInterceptor aspects");
    }
}