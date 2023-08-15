dependencyManagement {
    imports {
        mavenBom ("org.springframework.cloud:spring-cloud-dependencies:2021.0.8")
    }
}

dependencies {

    implementation(project(mapOf("path" to ":api-interface")))

    implementation("org.springframework.kafka:spring-kafka")
    implementation(project(mapOf("path" to ":api-interface:cash-process:cash-domain")))
    implementation(project(mapOf("path" to ":api-interface:cash-process:cash")))
    implementation("com.h2database:h2")

    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-actuator")
    api("org.springframework.boot:spring-boot-starter-validation")

    api ("org.springframework.cloud:spring-cloud-starter-openfeign")
    api("org.springframework.retry:spring-retry")
    api("org.springframework:spring-aspects")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}