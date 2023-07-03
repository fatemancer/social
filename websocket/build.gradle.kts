val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.8.10"
    id("org.springframework.boot") version "2.6.7"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("plugin.spring") version "1.6.21"
}

group = "hauu.info"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.spring.web)
    implementation(libs.spring.actuator)
    implementation("org.springframework:spring-websocket")
    implementation("org.springframework:spring-messaging")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-starter-security")

    // utils
    implementation("org.apache.commons:commons-lang3:3.12.0")

    // logs
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")

    // db (auth)
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
}