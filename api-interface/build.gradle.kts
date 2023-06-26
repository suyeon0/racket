object Versions {
	const val h2 = "2.1.214"
}

dependencies {
	// Test
	testImplementation(platform("org.junit:junit-bom:5.9.1"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	testImplementation("com.h2database:h2:${Versions.h2}")
}

tasks.test {
	useJUnitPlatform()
}