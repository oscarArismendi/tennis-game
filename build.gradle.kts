plugins {
    kotlin("jvm") version "2.1.21"
    application
}

group = "tennis.game"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-assertions-core-jvm:6.0.1")

    // database
    implementation("mysql:mysql-connector-java:8.0.11")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("MainKt")
}
