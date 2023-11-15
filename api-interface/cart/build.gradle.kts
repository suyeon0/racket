dependencyManagement {
    imports {
        mavenBom ("org.springframework.cloud:spring-cloud-dependencies:2021.0.8")
    }
}

object Versions {
    const val h2 = "2.1.214"
}

dependencies {
    implementation(project(":api-interface:api-shared:user"))
    implementation(project(":shared:util"))
    implementation(project(":shared:domain"))
    implementation(project(":api-interface"))

    //swagger
    implementation("org.springdoc:springdoc-openapi-ui:1.7.0")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.7.0")

    // Test
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("com.h2database:h2:${Versions.h2}")
    testImplementation("it.ozimov:embedded-redis:0.7.2")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("com.github.tomakehurst:wiremock:2.27.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}