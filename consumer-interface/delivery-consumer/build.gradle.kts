plugins {
    id("java")
}

group = "com.commerce"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.kafka:spring-kafka:2.8.1")
    implementation("com.h2database:h2")
    implementation ("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation(project(mapOf("path" to ":api-interface:delivery")))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}