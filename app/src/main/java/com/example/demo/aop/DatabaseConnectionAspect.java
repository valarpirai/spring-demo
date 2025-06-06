package com.example.demo.aop;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DatabaseConnectionAspect {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionAspect.class);

    private long startTime;

    @Before("execution(* javax.sql.DataSource.getConnection(..))")
    public void beforeGetConnection() {
        startTime = System.nanoTime();
        logger.info("Attempting to acquire database connection...");
    }

    @AfterReturning(
            pointcut = "execution(* javax.sql.DataSource.getConnection(..))",
            returning = "connection")
    public void afterGetConnection(Connection connection) {
        long acquisitionTime = System.nanoTime() - startTime;
        logger.info("Connection acquired in {} ms", acquisitionTime / 1_000_000.0);
    }

    @Before("execution(* java.sql.Connection.close(..))")
    public void beforeCloseConnection() {
        startTime = System.nanoTime();
        logger.info("Attempting to release database connection...");
    }

    @AfterReturning(pointcut = "execution(* java.sql.Connection.close(..))")
    public void afterCloseConnection() {
        long releaseTime = System.nanoTime() - startTime;
        logger.info("Connection released in {} ms", releaseTime / 1_000_000.0);
    }
}
