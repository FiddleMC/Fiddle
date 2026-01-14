version = "1.0.0-SNAPSHOT"

plugins {
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19"
}

repositories {
    mavenLocal()
}

dependencies {
    compileOnly(project(":fiddle-api"))
    paperweight.paperDevBundle("1.21.11-R0.1-SNAPSHOT", "org.fiddlemc.fiddle")
}

tasks.processResources {
    val props = mapOf(
        "version" to project.version,
        "apiversion" to "\"${rootProject.providers.gradleProperty("apiVersion").get()}\"",
    )
    inputs.properties(props)
    filesMatching("paper-plugin.yml") {
        expand(props)
    }
}
