#!/bin/bash
set -e

# Check if the sonarqube database exists, create it if it doesn't
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "postgres" -tAc "SELECT 1 FROM pg_database WHERE datname='sonarqube'" | grep -q 1 || \
    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "postgres" -c "CREATE DATABASE sonarqube WITH ENCODING 'UTF8' TEMPLATE template0;"

# Execute SQL to create schema and user in the sonarqube database
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "sonarqube" <<-EOSQL
    -- Create the sonarqube schema
    CREATE SCHEMA IF NOT EXISTS sonarqube;

    -- Create the sonarqube user
    CREATE ROLE sonarqube WITH LOGIN PASSWORD 'sonar_password';

    -- Grant privileges to the sonarqube user on the sonarqube schema
    GRANT USAGE ON SCHEMA sonarqube TO sonarqube;
    GRANT CREATE ON SCHEMA sonarqube TO sonarqube;

    -- Grant privileges for the sonarqube user to manage objects in the schema
    GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA sonarqube TO sonarqube;
    GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA sonarqube TO sonarqube;

    -- Set default privileges for future objects created in the schema
    ALTER DEFAULT PRIVILEGES IN SCHEMA sonarqube
        GRANT ALL ON TABLES TO sonarqube;
    ALTER DEFAULT PRIVILEGES IN SCHEMA sonarqube
        GRANT ALL ON SEQUENCES TO sonarqube;
EOSQL
