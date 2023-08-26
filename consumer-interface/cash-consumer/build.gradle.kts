dependencies {
    implementation(project(mapOf("path" to ":api-interface")))
    implementation(project(mapOf("path" to ":api-interface:cash-process:cash-domain")))
    implementation(project(mapOf("path" to ":api-interface:cash-process:cash")))

    implementation("org.springframework.kafka:spring-kafka")
    implementation("com.h2database:h2")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}