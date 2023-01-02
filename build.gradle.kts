import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.7"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    id("org.openapi.generator") version "6.2.1"
    id("org.liquibase.gradle") version "2.0.4"
}

val generatedRoot = "$rootDir/generated"

sourceSets {
    val main by getting
    main.java.srcDir("${generatedRoot}/src/main/kotlin")
    val test by getting
    test.java.srcDir("${generatedRoot}/src/main/kotlin")
}


group = "info.hauu"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    // core & codegen
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-ui:1.5.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("javax.servlet:javax.servlet-api:4.0.1")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    implementation("org.springframework.boot:spring-boot-starter-test")

    // db
    implementation("org.springframework:spring-jdbc")
    implementation("mysql:mysql-connector-java:8.0.30")

    // liquibase
    liquibaseRuntime("org.liquibase:liquibase-core:4.2.2")
    liquibaseRuntime("mysql:mysql-connector-java:5.1.34")

    // logs
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")



//    implementation("org.springframework.boot:spring-boot-starter-test")
//    testImplementation("org.springframework.boot:spring-boot-starter-test")
//    implementation("org.springframework.boot:spring-boot-starter-validation")
//    implementation("javax.validation:validation-api:2.0.1.Final")
//    implementation("io.swagger.core.v3:swagger-annotations:2.2.6")
//    implementation("io.swagger.core.v3:swagger-core:2.2.6")
//    implementation("io.swagger.core.v3:swagger-maven-plugin:2.2.6")
//    implementation("io.swagger.core.v3:swagger-jaxrs2:2.2.6")
//    implementation("javax.ws.rs:javax.ws.rs-api:2.1")
//    implementation("javax.servlet:javax.servlet-api:4.0.1")

//    implementation("io.swagger.core.v3:swagger-maven-plugin-jakarta:2.2.6")
//    implementation("io.swagger.core.v3:swagger-jaxrs2-jakarta:2.2.6")
//    implementation("jakarta.servlet:jakarta.servlet-api:5.0.0")
//    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
//    implementation("jakarta.ws.rs:jakarta.ws.rs-api:3.1.0")


}

tasks.openApiGenerate {
    dependsOn(":clean")
}

tasks.compileKotlin {
    dependsOn(":openApiGenerate")
}

openApiGenerate {
    generatorName.set("kotlin-spring")
    library.set("spring-boot")
    apiPackage.set("org.openapi.api")
    modelPackage.set("org.openapi.model")
    invokerPackage.set("info.hauu.highloadsocial")
    inputSpec.set("$rootDir/src/main/resources/openapi.json")
    outputDir.set(generatedRoot)
    additionalProperties.put("gradleBuildFile", true)
    configOptions.put("delegatePattern", "true")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
