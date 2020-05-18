import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.3.41"
    java
    `java-library`
    // Apply the application plugin to add support for building a CLI application.
    application
}
tasks.test {
    useJUnitPlatform()
}

repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("me.tongfei:progressbar:0.8.1")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter:5.5.2")

}

application {
    // Define the main class for the application.
    mainClassName = "rayTracingInAWeeking.ImageKt"
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}