import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.4"
	id("io.spring.dependency-management") version "1.0.14.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"
	kotlin("kapt") version "1.7.20" // querydsl dependency
}

group = "com.api"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

val queryDslVersion = "5.0.0"

sourceSets["main"].withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
	kotlin.srcDir("$buildDir/generated/source/kapt/main")
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.flywaydb:flyway-core")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("org.postgresql:postgresql")
	implementation("org.springframework.boot:spring-boot-configuration-processor")
//	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	// Jwt
	implementation("io.jsonwebtoken:jjwt-api:0.11.5") // jwt
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5") // jwt
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5") // jwt

	// Swagger
	implementation("org.springdoc:springdoc-openapi-ui:1.6.12")
	implementation("org.springdoc:springdoc-openapi-kotlin:1.6.12")
	implementation("org.springdoc:springdoc-openapi-data-rest:1.6.12")

	// Querydsl dependency
	implementation("com.querydsl:querydsl-jpa:$queryDslVersion")
	kapt("com.querydsl:querydsl-apt:$queryDslVersion:jpa")

	// Test Code
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude("org.junit.vintage", "junit-vintage-engine")
		exclude("org.mockito", "mockito-core")
	}
	testImplementation("org.springframework.amqp:spring-rabbit-test")
	testImplementation("org.springframework.security:spring-security-test")

	// Kotlin TEST CODE PACKAGE
	testImplementation("org.mockito:mockito-inline")
	testImplementation("org.junit.jupiter:junit-jupiter-engine")
	testImplementation("com.ninja-squad:springmockk:3.1.1")

	// AWS
	implementation("ca.pjer:logback-awslogs-appender:1.6.0")
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

tasks.getByName<Jar>("jar") {
  enabled = false
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
  this.archiveFileName.set("${archiveBaseName.get()}.${archiveExtension.get()}")
}

// Not working
//tasks.register<Copy>("Serve Deploy Jar") {
//	description = "Deploy jar to root"
//	dependsOn("assemble")
//	outputs.upToDateWhen { false }
//	from("build/libs") {
//		include("*.jar")
//	}
//	into("/")
//}