docker pull otel/opentelemetry-collector-contrib
docker run -d --rm -v ~/dev/demo/config.yaml:/etc/otelcol-contrib/config.yaml -v ~/dev/demo/example.log:/example.log -p 4317:4317 otel/opentelemetry-collector-contrib



export JAVA_TOOL_OPTIONS="-javaagent:~/dev/demo/opentelemetry-javaagent.jar"
export JAVA_TOOL_OPTIONS=""
export OTEL_EXPORTER_OTLP_TRACES_HEADERS="Authorization=Bearer cxtp_uLMjnsEbcbWhboxWvGNZpA65mimOTY"


export OTEL_TRACES_EXPORTER="otlp"
export OTEL_EXPORTER_OTLP_TRACES_PROTOCOL="grpc"
export OTEL_EXPORTER_OTLP_ENDPOINT="http://localhost:4317"
export OTEL_RESOURCE_ATTRIBUTES="service.name=Demo-Application,application.name=Demo-Application,api.name=Demo-Application,cx.application.name=Demo-Application,cx.subsystem.name=Graphql-Application"
export OTEL_RESOURCE_ATTRIBUTES="service.name=Galaxy-Application,application.name=Galaxy-Application,api.name=Accounts-web-Application,cx.application.name=Galaxy-Application,cx.subsystem.name=Accounts-web-Application"


./gradlew otel-extension:build
./gradlew build

java -javaagent:opentelemetry-javaagent.jar -Dio.opentelemetry.javaagent.slf4j.simpleLogger.defaultLogLevel=off -jar build/libs/demo-0.0.1.jar

java -javaagent:/Users/valarpiraichandran/dev/demo/opentelemetry-javaagent.jar -Dio.opentelemetry.javaagent.slf4j.simpleLogger.defaultLogLevel=off -jar accounts-web/build/libs/accounts-web.war

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
