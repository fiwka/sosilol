import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.0"
    id("io.spring.dependency-management") version "1.1.0"
    id("gg.jte.gradle") version "2.2.4"
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.spring") version "1.8.0"
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

group = "ru.kdev"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_19

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.session:spring-session-core:3.0.0")
    implementation("org.springframework.session:spring-session-data-redis:3.0.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
    implementation("org.hibernate.validator:hibernate-validator:8.0.0.Final")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.4")
    implementation("org.reactivestreams:reactive-streams:1.0.4")
    implementation("gg.jte:jte:2.2.4")
    implementation("org.json:json:20220320")
    implementation("io.lettuce:lettuce-core:6.2.2.RELEASE")
    implementation("org.postgresql:postgresql:42.5.3")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testImplementation("com.h2database:h2:2.1.214")
    testImplementation("it.ozimov:embedded-redis:0.7.3")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xlambdas=indy")
        jvmTarget = "19"
    }
}

jte {
    precompile()
}

tasks.bootJar {
    dependsOn(tasks.precompileJte)
    with(bootInf) {
        from(fileTree("jte-classes") {
            include("**/*.class")
        })
        into("classes")
    }
}

configurations.all {
    exclude(module = "logback-classic")
    exclude(module = "spring-boot-starter-logging")
    exclude(module = "log4j-to-slf4j")
}

tasks.test {
    useJUnitPlatform()

    maxHeapSize = "1G"
}

tasks.bootRun {
    systemProperty("file.encoding", "UTF-8")
}