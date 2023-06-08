import io.papermc.paperweight.tasks.ZippedTask
import io.papermc.paperweight.util.path
import io.papermc.paperweight.util.pathOrNull
import org.gradle.configurationcache.extensions.capitalized
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteRecursively
import kotlin.io.path.readLines
import kotlin.io.path.writeLines

plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
    id("io.papermc.paperweight.patcher") version "1.4.0"
    id("com.github.ManifestClasspath") version "0.1.0-RELEASE"
}

val paperMavenPublicUrl = "https://repo.papermc.io/repository/maven-public/"

repositories {
    mavenCentral()
    maven(paperMavenPublicUrl) {
        content { onlyForConfigurations(configurations.paperclip.name) }
    }
}

dependencies {
    remapper("net.fabricmc:tiny-remapper:0.8.6:fat")
    decompiler("net.minecraftforge:forgeflower:2.0.605.1")
    paperclip("io.papermc:paperclip:3.0.2")
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
}

subprojects {
    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    tasks.withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }
    tasks.withType<ProcessResources> {
        filteringCharset = Charsets.UTF_8.name()
    }

    repositories {
        mavenCentral()
        maven(paperMavenPublicUrl)
    }
}

paperweight {
    serverProject.set(project(":fiddle-server")) // Fiddle - build changes

    remapRepo.set(paperMavenPublicUrl)
    decompileRepo.set(paperMavenPublicUrl)

    usePaperUpstream(providers.gradleProperty("paperRef")) {
        withPaperPatcher {
            apiPatchDir.set(layout.projectDirectory.dir("patches/api"))
            apiOutputDir.set(layout.projectDirectory.dir("fiddle-api")) // Fiddle - build changes

            serverPatchDir.set(layout.projectDirectory.dir("patches/server"))
            serverOutputDir.set(layout.projectDirectory.dir("fiddle-server")) // Fiddle - build changes
        }
    }
}

// Fiddle start - extend jar manifest
val extendedManifestElements: List<Pair<String, String>> = listOf(
    "Add-Opens" to "java.base/java.lang" // Fiddle - remove hard-coded Bukkit values - modify classes - add module opens to server jar
)

if (extendedManifestElements.isNotEmpty()) {
    for (classifier in arrayOf("mojmap", "reobf")) {
        // Based on io.papermc.paperweight.taskcontainers.BundlerJarTasks
        tasks.named("create${classifier.capitalized()}BundlerJar") {
            doLast {

                // Based on io.papermc.paperweight.taskcontainers.BundlerJarTasks
                val jarName = listOfNotNull(
                    project.name,
                    "bundler",
                    project.version,
                    classifier
                ).joinToString("-") + ".jar"

                // Based on io.papermc.paperweight.taskcontainers.BundlerJarTasks
                val zipFile = layout.buildDirectory.file("libs/$jarName").path

                val rootDir = io.papermc.paperweight.util.findOutputDir(zipFile)

                try {
                    io.papermc.paperweight.util.unzip(zipFile, rootDir)

                    val manifestFile = rootDir.resolve("META-INF/MANIFEST.MF")
                    manifestFile.writeLines(ArrayList(manifestFile.readLines()).apply {
                        addAll(
                            indexOfFirst { it.startsWith("Main-Class:") } + 1,
                            extendedManifestElements.map { "${it.first}: ${it.second}" }
                        )
                    })

                    io.papermc.paperweight.util.ensureDeleted(zipFile)

                    io.papermc.paperweight.util.zip(rootDir, zipFile)
                } finally {
                    @OptIn(kotlin.io.path.ExperimentalPathApi::class)
                    rootDir.deleteRecursively()
                }

            }
        }
    }
}
// Fiddle end - extend jar manifest
