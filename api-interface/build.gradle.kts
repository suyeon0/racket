object Versions {
	const val h2 = "2.1.214"
}

dependencies {
	implementation(project(mapOf("path" to ":shared:domain")))
	// DB
	implementation ("com.mysql:mysql-connector-j")

	//Redis
	implementation("org.springframework.boot:spring-boot-starter-data-redis:2.5.4")

	//thymeleaf
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

	//swagger
	implementation("org.springdoc:springdoc-openapi-ui:1.7.0")
	implementation("org.springdoc:springdoc-openapi-kotlin:1.7.0")

    // Test
	testImplementation(platform("org.junit:junit-bom:5.9.1"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	testImplementation("com.h2database:h2:${Versions.h2}")
	testImplementation("it.ozimov:embedded-redis:0.7.2")
}

tasks.test {
	useJUnitPlatform()
}