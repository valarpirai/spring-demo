docker pull otel/opentelemetry-collector-contrib
docker run -d --rm -v ~/dev/demo/config.yaml:/etc/otelcol-contrib/config.yaml -v ~/dev/demo/example.log:/example.log -p 4317:4317 otel/opentelemetry-collector-contrib

export OTEL_SERVICE_NAME="Demo-Application"
export OTEL_TRACES_EXPORTER="otlp"
export OTEL_EXPORTER_OTLP_TRACES_PROTOCOL="grpc"
export OTEL_EXPORTER_OTLP_ENDPOINT="http://localhost:4317"
export OTEL_RESOURCE_ATTRIBUTES="service.name=Demo-Application,application.name=Demo-Application,api.name=Demo-Application,cx.application.name=Demo-Application,cx.subsystem.name=Graphql-Application"

export OTEL_RESOURCE_ATTRIBUTES="service.name=Galaxy-Application,application.name=accounts-web,api.name=Accounts-web-Application,cx.application.name=accounts-web,cx.subsystem.name=Accounts-web-Application"


./gradlew build

java -javaagent:opentelemetry-javaagent.jar -Dio.opentelemetry.javaagent.slf4j.simpleLogger.defaultLogLevel=off -jar build/libs/demo-0.0.1.jar

Collect TraceId
Endpoint
Enable based on conditions/feature flag

Search

docker run -d --rm \
  -e COLLECTOR_ZIPKIN_HOST_PORT=:9411 \
  -p 16686:16686 \
  -p 4317:4317 \
  -p 4318:4318 \
  -p 9411:9411 \
  jaegertracing/all-in-one:latest


./gradlew accounts-web:build

java -javaagent:opentelemetry-javaagent.jar -Dio.opentelemetry.javaagent.slf4j.simpleLogger.defaultLogLevel=off -jar accounts-web/build/libs/accounts-web.war


java -javaagent:/Users/valarpiraichandran/spring-startup-analyzer/lib/spring-profiler-agent.jar -Dspring-startup-analyzer.admin.http.server.port=8066 -jar 

