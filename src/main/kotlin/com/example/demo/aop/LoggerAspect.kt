package com.example.demo.aop

import brave.Tracer
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.CodeSignature
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import io.opentelemetry.api.trace.Span
import io.opentelemetry.instrumentation.api.instrumenter.LocalRootSpan
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.context.annotation.Configuration

@Configuration
@Aspect
class LoggerAspect {
    @Autowired
    private lateinit var tracer: Tracer

    private val logger: Logger = LoggerFactory.getLogger(LoggerAspect::class.java)

    @Before("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    fun before(jointPoint: JoinPoint) {
        val rootSpan = LocalRootSpan.current()
        rootSpan.setAttribute("app.force_sample", true)
        logger.info("Before all aspects");
    }

    @Before("@annotation(org.springframework.graphql.data.method.annotation.QueryMapping)")
    fun preProcessGraphQLEndpoint(joinPoint: JoinPoint) {
        val signature = joinPoint.signature as CodeSignature

        val rootSpan = LocalRootSpan.current()
        rootSpan.setAttribute("app.force_sample", true)
        rootSpan.setAttribute("endPoint", signature.name)
        rootSpan.setAttribute("tenantId", "1234")


        val span: Span = Span.current()
        val context = span.spanContext
        logger.info("TraceId ->" + context.traceId);
        logger.info("SpanId ->" + context.spanId);
    }

    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    fun around(jointPoint: ProceedingJoinPoint): Any? {
        logger.info("*** Before Request ***");
        val res = jointPoint.proceed();
        logger.info("After Request")
        return res;
    }

    @After("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    fun after(jointPoint: JoinPoint) {
        logger.info("After all aspects");
    }
}