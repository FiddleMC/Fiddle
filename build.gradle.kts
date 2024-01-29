import io.papermc.paperweight.util.path
import org.gradle.configurationcache.extensions.capitalized
import kotlin.io.path.deleteRecursively
import kotlin.io.path.readLines
import kotlin.io.path.writeLines

plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
    id("io.papermc.paperweight.patcher") version "1.5.11"
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
    remapper("net.fabricmc:tiny-remapper:0.8.10:fat")
    decompiler("net.minecraftforge:forgeflower:2.0.627.2")
    paperclip("io.papermc:paperclip:3.0.3")
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(17)
        }
    }
}

subprojects {
    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release = 17
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
    serverProject = project(":fiddle-server") // Fiddle - build changes

    remapRepo = paperMavenPublicUrl
    decompileRepo = paperMavenPublicUrl

    usePaperUpstream(providers.gradleProperty("paperRef")) {
        withPaperPatcher {
            apiPatchDir = layout.projectDirectory.dir("patches/api")
            apiOutputDir = layout.projectDirectory.dir("fiddle-api") // Fiddle - build changes

            serverPatchDir = layout.projectDirectory.dir("patches/server")
            serverOutputDir = layout.projectDirectory.dir("fiddle-server") // Fiddle - build changes
        }
    }
}

// Uncomment while updating for a new Minecraft version
//tasks.withType<CollectATsFromPatches> {
//    extraPatchDir.set(layout.projectDirectory.dir("patches/unapplied/server"))
//}

// Fiddle start - branding changes - license - package into jar
for (classifier in arrayOf("mojmap", "reobf")) {
    // Based on io.papermc.paperweight.taskcontainers.BundlerJarTasks
    tasks.named("create${classifier.capitalized()}PaperclipJar") {
        doLast {

            // Based on io.papermc.paperweight.taskcontainers.BundlerJarTasks
            val jarName = listOfNotNull(
                project.name,
                "paperclip",
                project.version,
                classifier
            ).joinToString("-") + ".jar"

            // Based on io.papermc.paperweight.taskcontainers.BundlerJarTasks
            val zipFile = layout.buildDirectory.file("libs/$jarName").path

            val rootDir = io.papermc.paperweight.util.findOutputDir(zipFile)

            try {
                io.papermc.paperweight.util.unzip(zipFile, rootDir)

                val licenseFileName = "LICENSE.txt"
                project(":fiddle-server").projectDir.resolve(licenseFileName).copyTo(rootDir.resolve(licenseFileName).toFile())

                io.papermc.paperweight.util.ensureDeleted(zipFile)

                io.papermc.paperweight.util.zip(rootDir, zipFile)
            } finally {
                @OptIn(kotlin.io.path.ExperimentalPathApi::class)
                rootDir.deleteRecursively()
            }

        }
    }
}
// Fiddle end - branding changes - license - package into jar

// Fiddle start - extend jar manifest
val extendedManifestElements: List<Pair<String, String>> = listOf(
    "Add-Opens" to "java.base/java.lang" // Fiddle - modifiable Bukkit enums - inject runtime versions - add module opens to server jar
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
