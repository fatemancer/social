import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.2-SNAPSHOT"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
    id("org.openapi.generator") version "6.2.1"
}

group = "info.hauu"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.openApiGenerate {
    dependsOn(":clean")
}

tasks.compileKotlin {
    dependsOn (":openApiGenerate")
}

openApiGenerate {
    generatorName.set("kotlin-spring")
    library.set("spring-boot")
    apiPackage.set("org.openapi.api")
    modelPackage.set("org.openapi.model")
    invokerPackage.set("info.hauu.highloadsocial")
    inputSpec.set("$rootDir/src/main/resources/openapi.json")
    outputDir.set("$rootDir/generated/src")
    additionalProperties.put("gradleBuildFile", false)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
