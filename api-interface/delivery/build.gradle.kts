plugins {
    id("java")
}

group = "com.commerce"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    implementation(project(":api-interface"))
    implementation(project(":api-interface:api-shared:user"))

    // mongodb
    implementation ("org.springframework.boot:spring-boot-starter-data-mongodb")
    // kafka
    implementation("org.springframework.kafka:spring-kafka")
    // util
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")

    //swagger
    implementation("org.springdoc:springdoc-openapi-ui:1.7.0")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.7.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}