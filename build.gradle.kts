plugins {
    kotlin("jvm") version "1.8.21" apply(false)
    id("maven-publish")
}

val devJar by tasks.registering(Jar::class) {
    archiveFileName = "plugindev.jar"
    destinationDirectory = layout.buildDirectory.dir("lib/")
    val modelClasses = fileTree("$rootDir/model/build/classes/kotlin/main")
    val pluginDevClasses = fileTree("$rootDir/plugindev/build/classes/kotlin/main")

    from(modelClasses, pluginDevClasses)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.github.justincodinguk.c_hat_server_v2"
            artifactId = "plugindev"
            version = "0.0.1"

            dependencies { devJar }
            artifact(devJar.get().outputs.files.asPath)
        }
    }
}

