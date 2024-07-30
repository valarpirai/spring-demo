plugins {
	id("org.springframework.boot") version "2.7.0"
	id("io.spring.dependency-management") version "1.1.5"
	kotlin("jvm") version "1.5.31"
	kotlin("plugin.jpa") version "1.5.31"
	kotlin("plugin.spring") version "1.5.31"
}

group = "com.example"
version = "0.0.1"

java {
//	toolchain {
//		languageVersion = JavaLanguageVersion.of(17)
//	}
	sourceCompatibility = JavaVersion.VERSION_1_8
}

repositories {
	mavenCentral()
	mavenLocal()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-cache")
	runtimeOnly("com.mysql:mysql-connector-j:8.3.0")
	implementation(platform("org.springframework.cloud:spring-cloud-dependencies:2021.0.3"))
	implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
	implementation("org.springframework.boot:spring-boot-starter-graphql")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springframework.boot:spring-boot-starter-quartz")

	implementation("com.launchdarkly:launchdarkly-java-server-sdk:7.0.0")

//	implementation("org.disposableemail:org.disposable:0.0.1")
	implementation(project("disposable-email"))

	implementation("org.jetbrains.kotlin:kotlin-reflect")

	implementation("org.modelmapper:modelmapper:3.2.0")
	implementation("io.opentelemetry:opentelemetry-api:1.16.0")
	implementation("io.opentelemetry.instrumentation:opentelemetry-instrumentation-api:2.4.0")
	implementation("io.opentelemetry.instrumentation:opentelemetry-instrumentation-annotations:2.4.0")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("com.graphql-java-kickstart:graphql-spring-boot-starter-test:6.0.0")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	implementation(project("path" to "shadow-local", "configuration" to "shadow"))

	testImplementation("org.mockito:mockito-core:2.19.0")
	testImplementation("org.mockito:mockito-junit-jupiter:2.19.0")
}

//kotlin {
//	compilerOptions {
//		freeCompilerArgs.addAll("-Xjsr305=strict")
//	}
//}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
