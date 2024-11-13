plugins {
    kotlin("jvm") version "2.0.20"
}

group = "org.poach3r"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.kgit2:kommand:2.2.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}