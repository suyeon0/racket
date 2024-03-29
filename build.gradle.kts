import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.11"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.3.72"
}

allprojects {
	group = "com.commerce"
	version = "0.0.1-SNAPSHOT"

	repositories {
		mavenCentral()
	}
}

ext {
	set("springCloudVersion", "2021.0.8")
}

subprojects {
	apply(plugin = "kotlin")
	apply(plugin = "kotlin-spring")

	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "org.jetbrains.kotlin.plugin.spring")
	apply(plugin = "org.jetbrains.kotlin.plugin.jpa")

	dependencies {
		//spring boot
		implementation("org.springframework.boot:spring-boot-starter-web")
		implementation("org.springframework.boot:spring-boot-starter-actuator")
		implementation("org.springframework.boot:spring-boot-starter-validation")

		//kotlin
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
		implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

		//util
		implementation("org.apache.commons:commons-lang3")
		implementation ("org.projectlombok:lombok:1.18.20")
		implementation("io.github.microutils:kotlin-logging:2.1.23")
		implementation("org.slf4j:slf4j-api:1.7.30")

		//test
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		testImplementation("org.springframework.security:spring-security-test")
		testImplementation("org.junit.jupiter:junit-jupiter-params")

		//DB
		implementation("org.springframework.boot:spring-boot-starter-data-jpa")
		implementation("org.javassist:javassist:3.29.2-GA")

		//spring-retry
		implementation("org.springframework.retry:spring-retry")
		implementation("org.springframework:spring-aspects")

		// cache
		implementation("org.springframework.boot:spring-boot-starter-cache:3.1.2")

		// feign
		implementation ("org.springframework.cloud:spring-cloud-starter-openfeign")
	}

	dependencyManagement {
		imports {
			mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
			mavenBom("org.springframework.cloud:spring-cloud-dependencies:2021.0.8")
		}

		dependencies {
			dependency("net.logstash.logback:logstash-logback-encoder:6.6")
		}
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "17"
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}

	configurations {
		compileOnly {
			extendsFrom(configurations.annotationProcessor.get())
		}
	}
}