plugins {
    kotlin("jvm") version "2.1.21"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.fsp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.github.ajalt.clikt:clikt:4.2.1")
}

kotlin {
    jvmToolchain(21)
}

tasks.shadowJar {
    archiveBaseName.set("arbre")
    archiveClassifier.set("")
    archiveVersion.set("")
    manifest {
        attributes["Main-Class"] = "ArbreCliKt"
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}