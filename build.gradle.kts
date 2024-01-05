plugins {
    kotlin("jvm") version "1.8.21" apply(false)
}

val annotationsJar by tasks.registering(Jar::class) {
    dependsOn(":annotations:jar")
    from("$rootDir/annotations/build/classes/java/main")
}

val modelJar by tasks.registering(Jar::class) {
    dependsOn(":model:jar")
    from("$rootDir/model/build/classes/java/main")
}

val eventBusJar by tasks.registering(Jar::class) {
    dependsOn(":eventbus:jar")
    from("$rootDir/eventbus/build/classes/java/main")
}

val processorsJar by tasks.registering(Jar::class) {
    dependsOn(":processors:jar")
    from("$rootDir/processors/build/classes/java/main")
}

val combinedJar by tasks.registering(Jar::class) {
    dependsOn(modelJar, eventBusJar, annotationsJar)
    archiveFileName.set("c_hat_server_v2-plugindev.jar")
    from(modelJar, eventBusJar, annotationsJar)
}
