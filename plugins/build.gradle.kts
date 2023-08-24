import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("kapt")
}

group = "io.github.justincodinguk.c_hat_server_v2"
version = "0.0.1"

repositories {
    mavenCentral()
}

sourceSets {
    main {
        kotlin {
            srcDir("${buildDir.absolutePath}/generated/source/kaptKotlin/")
        }
    }
}

dependencies {
    implementation(project(":annotations"))
    kapt(project(":processors"))
    implementation(project(":eventbus"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}