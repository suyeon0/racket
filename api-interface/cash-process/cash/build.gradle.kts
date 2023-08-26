dependencyManagement {
    imports {
        mavenBom ("org.springframework.cloud:spring-cloud-dependencies:2021.0.8")
    }
}

object Versions {
    const val h2 = "2.1.214"
}

dependencies {
    implementation("org.springframework.kafka:spring-kafka")
    implementation(project(mapOf("path" to ":api-interface:cash-process:cash-domain")))
    implementation(project(mapOf("path" to ":api-interface:api-shared:user")))
    implementation(project(mapOf("path" to ":api-interface:api-shared:payment")))
    implementation(project(mapOf("path" to ":shared:util")))
    implementation(project(mapOf("path" to ":api-interface")))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    testImplementation("com.h2database:h2:${Versions.h2}")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}