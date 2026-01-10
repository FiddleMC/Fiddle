import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("io.papermc.paperweight.patcher") version "2.0.0-beta.19"
}

paperweight {
    upstreams.paper {
        ref = providers.gradleProperty("paperRef")

        patchFile {
            path = "paper-server/build.gradle.kts"
            // Fiddle start - Project setup - Set up Paperweight
            outputFile = file("fiddle-server/build.gradle.kts")
            patchFile = file("fiddle-server/build.gradle.kts.patch")
            // Fiddle end - Project setup - Set up Paperweight
        }
        patchFile {
            path = "paper-api/build.gradle.kts"
            // Fiddle start - Project setup - Set up Paperweight
            outputFile = file("fiddle-api/build.gradle.kts")
            patchFile = file("fiddle-api/build.gradle.kts.patch")
            // Fiddle end - Project setup - Set up Paperweight
        }
        patchDir("paperApi") {
            upstreamPath = "paper-api"
            excludes = setOf("build.gradle.kts")
            patchesDir = file("fiddle-api/paper-patches") // Fiddle - Project setup - Set up Paperweight
            outputDir = file("paper-api")
        }
    }
}

val paperMavenPublicUrl = "https://repo.papermc.io/repository/maven-public/"

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(25) // Fiddle - Project setup - Java 25
        }
    }

    repositories {
        mavenCentral()
        maven(paperMavenPublicUrl)
    }

    tasks.withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }
    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release = 25 // Fiddle - Project setup - Java 25
        options.isFork = true
        // Fiddle start - Project setup - Hide annoying compilation warnings
        options.compilerArgs.addAll(
            listOf(
                "-Xlint:-dep-ann",
                "-Xlint:-deprecation",
                "-Xlint:-module",
                "-Xlint:-removal",
            )
        )
        // Fiddle end - Project setup - Hide annoying compilation warnings
    }
    tasks.withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }
    tasks.withType<ProcessResources> {
        filteringCharset = Charsets.UTF_8.name()
    }
    tasks.withType<Test> {
        testLogging {
            showStackTraces = true
            exceptionFormat = TestExceptionFormat.FULL
            events(TestLogEvent.STANDARD_OUT)
        }
    }

    extensions.configure<PublishingExtension> {
        repositories {
            /*
            maven("https://repo.papermc.io/repository/maven-snapshots/") {
                name = "paperSnapshots"
                credentials(PasswordCredentials::class)
            }
             */
        }
    }
}
