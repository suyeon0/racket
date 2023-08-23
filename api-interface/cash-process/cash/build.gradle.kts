dependencyManagement {
    imports {
        mavenBom ("org.springframework.cloud:spring-cloud-dependencies:2021.0.8")
    }
}

object Versions {
    const val h2 = "2.1.214"
}

dependencies {
    implementation(project(mapOf("path" to ":api-interface:cash-process:cash-domain")))
    implementation(project(mapOf("path" to ":api-interface")))

    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-actuator")
    api("org.springframework.boot:spring-boot-starter-validation")
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api ("com.mysql:mysql-connector-j")
    api("org.javassist:javassist:3.29.2-GA")
    api ("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.kafka:spring-kafka")
    api ("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation(project(mapOf("path" to ":api-interface:cash-process:cash-domain")))
    implementation(project(mapOf("path" to ":api-interface:api-shared:user")))
    implementation(project(mapOf("path" to ":api-interface:api-shared:payment")))
    implementation(project(mapOf("path" to ":shared")))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    testImplementation("com.h2database:h2:${Versions.h2}")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}