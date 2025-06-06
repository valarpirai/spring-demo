package com.example.demo.configuration;

import com.zaxxer.hikari.HikariDataSource;
import datadog.trace.api.Trace;
import java.sql.Connection;
import java.sql.ConnectionBuilder;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.ShardingKeyBuilder;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.springframework.stereotype.Component;

@Component
public class TimedDataSource implements DataSource {
    private final HikariDataSource delegate;

    public TimedDataSource(HikariDataSource dataSource) {
        this.delegate = dataSource;
    }

    @Trace(operationName = "db.connection.acquire", resourceName = "HikariPool acquire")
    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = delegate.getConnection();
        return new InstrumentedDbConnection(connection, delegate.getPoolName());
    }

    // Delegate other DataSource methods
    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return delegate.getConnection(username, password);
    }

    @Override
    public java.io.PrintWriter getLogWriter() throws SQLException {
        return delegate.getLogWriter();
    }

    @Override
    public void setLogWriter(java.io.PrintWriter out) throws SQLException {
        delegate.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        delegate.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return delegate.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public ShardingKeyBuilder createShardingKeyBuilder() throws SQLException {
        return DataSource.super.createShardingKeyBuilder();
    }

    @Override
    public ConnectionBuilder createConnectionBuilder() throws SQLException {
        return DataSource.super.createConnectionBuilder();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return delegate.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return delegate.isWrapperFor(iface);
    }
}
