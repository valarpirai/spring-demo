package com.example.demo.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConnectionMetrics {

    private final MeterRegistry meterRegistry;

    @Autowired
    public ConnectionMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void recordAcquisitionTime(long acquisitionTimeNs) {
        meterRegistry.timer("db.connection.acquisition.time", "pool", "MyHikariPool")
                .record(acquisitionTimeNs, java.util.concurrent.TimeUnit.NANOSECONDS);
    }

    public void recordReleaseTime(long releaseTimeNs) {
        meterRegistry.timer("db.connection.release.time", "pool", "MyHikariPool")
                .record(releaseTimeNs, java.util.concurrent.TimeUnit.NANOSECONDS);
    }
}
