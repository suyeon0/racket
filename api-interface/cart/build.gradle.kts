dependencyManagement {
    imports {
        mavenBom ("org.springframework.cloud:spring-cloud-dependencies:2021.0.8")
    }
}

object Versions {
    const val h2 = "2.1.214"
}

dependencies {
    implementation(project(mapOf("path" to ":api-interface:api-shared:user")))
    implementation(project(mapOf("path" to ":shared:util")))
    implementation(project(mapOf("path" to ":api-interface")))

    // Test
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("com.h2database:h2:${Versions.h2}")
    testImplementation("it.ozimov:embedded-redis:0.7.2")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}