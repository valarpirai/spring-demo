package com.example.demo.aop

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration

@Configuration
@Aspect
class LoggerAspect {

    private val logger: Logger = LoggerFactory.getLogger(LoggerAspect::class.java)

    @Before("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    fun before(jointPoint: JoinPoint) {
        logger.info("Before all aspects");
    }

    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    fun around(jointPoint: ProceedingJoinPoint): Any? {
        logger.info("Before Request");
        val res = jointPoint.proceed();
        logger.info("After Request")
        return res;
    }

    @After("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    fun after(jointPoint: JoinPoint) {
        logger.info("After all aspects");
    }
}