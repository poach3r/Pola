plugins {
    kotlin("jvm") version "2.0.20"
    id("com.gradleup.shadow") version "8.3.5"
    id("org.graalvm.buildtools.native") version "0.10.3"
}

group = "org.poach3r"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

kotlin {
    jvmToolchain(21)
}

tasks {
    shadowJar {
        archiveBaseName.set("pola")
        archiveVersion.set("0.1.0")
        manifest {
            attributes["Main-Class"] = "org.poach3r.MainKt"
        }
    }

    graalvmNative {
        binaries {
            named("main") {
                imageName.set("pola")
                mainClass.set("org.poach3r.MainKt")
                buildArgs.add("-O4")
            }
        }
    }
}
