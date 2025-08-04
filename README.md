# Spring boot learning project
Learn Spring boot through examples


### Java Style guide setup
- `./gradlew spotlessCheck` - Check for Google Java Style
- `./gradlew spotlessApply` - Apply Google Java Style changes

### Java static analysis setup
Run SonarQube community server
```
docker run -d --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true -p 9000:9000 sonarqube:community
```

Steps:

- Goto http://localhost:9000/
- Login using admin/admin
- Create project
- Create API Token and configure in build.gradle
- Run the following command

`./gradlew sonar` - Run sonar scanning

### Protobuf Setup
Protocol Buffers (.proto files) are located in `src/main/protobuf/` directory.

#### Generating Java Classes from .proto files

**Prerequisites:**
Install Protocol Buffer compiler:
- macOS: `brew install protobuf`
- Ubuntu/Debian: `sudo apt-get install protobuf-compiler`
- Windows: Download from [Protocol Buffers releases](https://github.com/protocolbuffers/protobuf/releases)

**Generate Java classes:**
```bash
protoc --java_out=src/main/java --proto_path=src/main/protobuf src/main/protobuf/*.proto
```

**Current proto files:**
- `user_message.proto` - Defines UserMessage structure for Kafka messaging

**Kafka Protobuf Integration:**
- Producer endpoint: `POST /send-protobuf`
- Consumer automatically processes protobuf messages from `user-protobuf-topic`
