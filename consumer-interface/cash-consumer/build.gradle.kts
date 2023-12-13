dependencies {
    implementation("org.springframework.kafka:spring-kafka")
    implementation("com.h2database:h2")
    implementation ("org.springframework.boot:spring-boot-starter-data-mongodb")

    implementation(project(":api-interface"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}