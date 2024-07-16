import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.example"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.intuit.quickbooks-online:ipp-v3-java-data:6.1.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<ShadowJar>() {
    relocate("org.joda.time", "com.superops.shaded.org.joda.time")
    relocate("org.jvnet", "com.superops.shaded.org.jvnet")
}
