package com.example.demo.configuration;

import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HikariMetricsConfig {

    @Autowired
    public void bindHikariMetrics(HikariDataSource dataSource, MeterRegistry meterRegistry) {
        dataSource.setMetricRegistry(meterRegistry);
    }
}
