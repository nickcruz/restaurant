import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.20"
}

group = "nickcruz.me"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


val test by tasks.getting(Test::class) {
    useJUnitPlatform { }
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.1")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.2.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
