
dependencyManagement {
    imports {
        mavenBom ("org.springframework.cloud:spring-cloud-dependencies:2021.0.8")
    }
}

dependencies {
    implementation(project(mapOf("path" to ":api-interface:cache-process:domain")))
    implementation(project(mapOf("path" to ":api-interface")))

    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-actuator")
    api("org.springframework.boot:spring-boot-starter-validation")

    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api ("com.mysql:mysql-connector-j")
    api("org.javassist:javassist:3.29.2-GA")
    api ("org.springframework.boot:spring-boot-starter-data-mongodb")

    api ("org.springframework.cloud:spring-cloud-starter-openfeign")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}